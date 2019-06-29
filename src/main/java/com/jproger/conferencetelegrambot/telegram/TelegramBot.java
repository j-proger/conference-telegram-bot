package com.jproger.conferencetelegrambot.telegram;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.entities.Question;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TelegramBot extends TelegramLongPollingBot {
    private final ContactAPI contactAPI;
    private final QuestionAPI questionAPI;
    private final String name;
    private final String token;
    private final Map<Integer, UserWorkflow> usersWorkflow = new HashMap<>();

    public TelegramBot(ContactAPI contactAPI,
                       QuestionAPI questionAPI,
                       String name,
                       String token,
                       DefaultBotOptions options) {
        super(options);

        this.name = name;
        this.token = token;
        this.contactAPI = contactAPI;
        this.questionAPI = questionAPI;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String messageText = message.getText();

        if (Objects.nonNull(messageText) && messageText.toLowerCase().startsWith("/start")) {
            initUserWorkflow(update);
        } else if (message.hasContact()) {
            createContact(update);
            updateWorkflowToQuestion(update);
        } else {
            createQuestion(update);
        }
    }

    private void initUserWorkflow(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();
        UserWorkflow userWorkflow = usersWorkflow.get(user.getId());
        String topicKey = null;
        String[] commandParts = update.getMessage().getText().split(" ");

        if(commandParts.length > 1) {
            topicKey = commandParts[1];
        }

        if (Objects.isNull(userWorkflow)) {
            userWorkflow = UserWorkflow.builder()
                    .id(user.getId())
                    .chatId(chatId)
                    .state(UserWorkflow.State.SHARE_CONTACT)
                    .topicKey(topicKey)
                    .build();

            usersWorkflow.put(user.getId(), userWorkflow);

            requestContact(update);
        } else {
            com.jproger.conferencetelegrambot.entities.Contact contact = contactAPI.getContactByTelegramID(user.getId().toString());

            if(Objects.nonNull(topicKey)) {
                userWorkflow.setTopicKey(topicKey);
            }

            sendMessage(chatId, "Hello " + contact.getName() + "!");
        }
    }

    private void requestContact(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Share contact");

        button.setRequestContact(Boolean.TRUE);

        row.add(button);
        keyboard.add(row);
        markup.setKeyboard(keyboard);

        message.setReplyMarkup(markup);
        message.setChatId(chatId);
        message.setText("Can I have your phone number please");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Request contact exception");
            e.printStackTrace();
        }
    }

    private void createContact(Update update) {
        Integer userId = update.getMessage().getFrom().getId();
        UserWorkflow userWorkflow = usersWorkflow.get(userId);

        Objects.requireNonNull(userWorkflow, "User workflow not found");

        Contact tgContact = update.getMessage().getContact();
        List<String> fullNameParts = Stream.of(tgContact.getLastName(), tgContact.getFirstName())
                .filter(Objects::nonNull).collect(Collectors.toList());

        com.jproger.conferencetelegrambot.entities.Contact contact = com.jproger.conferencetelegrambot.entities.Contact.builder()
                .name(String.join(" ", fullNameParts))
                .phoneNumber(tgContact.getPhoneNumber())
                .telegramID(userId.toString())
                .build();

        contactAPI.addContact(contact);
    }

    private void updateWorkflowToQuestion(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer userId = update.getMessage().getFrom().getId();
        UserWorkflow userWorkflow = usersWorkflow.get(userId);

        Objects.requireNonNull(userWorkflow, "User workflow not found");

        userWorkflow.setState(UserWorkflow.State.ASK_QUESTION);
        sendMessage(chatId, "Congratulations, now you can ask questions! Each your message will be make a question.");
    }

    private void createQuestion(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer userId = update.getMessage().getFrom().getId();
        String questionText = update.getMessage().getText();

        UserWorkflow userWorkflow = usersWorkflow.get(userId);

        Objects.requireNonNull(userWorkflow, "User workflow not found");

        if (userWorkflow.getState() != UserWorkflow.State.ASK_QUESTION)
            throw new IllegalStateException("User can't ask questions");

        if(Objects.nonNull(userWorkflow.getTopicKey())) {
            Question question = Question.builder()
                    .question(questionText)
                    .topicKey(userWorkflow.getTopicKey())
                    .author(contactAPI.getContactByTelegramID(userId.toString()))
                    .build();

            questionAPI.addQuestion(question);

            sendMessage(chatId, "Your question registered. You can make new questions.");
        } else {
            sendMessage(chatId, "Please scan QR code from agende and try ask your question again.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setText(text);
        message.setChatId(chatId);
        message.setReplyMarkup(new ReplyKeyboardRemove());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message couldn't send");
            e.printStackTrace();
        }
    }
}
