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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

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
            requestContact(update);
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

        UserWorkflow userWorkflow = UserWorkflow.builder()
                .id(user.getId())
                .chatId(chatId)
                .state(UserWorkflow.State.SHARE_CONTACT)
                .build();

        usersWorkflow.put(user.getId(), userWorkflow);
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
        message.setText("Get me your phone number please");

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

        com.jproger.conferencetelegrambot.entities.Contact contact = com.jproger.conferencetelegrambot.entities.Contact.builder()
                .name(String.join(" ", tgContact.getLastName(), tgContact.getFirstName()))
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

        Question question = Question.builder()
                .question(questionText)
                .author(contactAPI.getContctByTelegramID(userId.toString()))
                .build();

        questionAPI.addQuestion(question);

        sendMessage(chatId, "Your question registered. You can make new questions.");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setText(text);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message couldn't send");
            e.printStackTrace();
        }
    }
}
