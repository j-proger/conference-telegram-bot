package com.jproger.conferencetelegrambot.channels.telegram;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;
import com.jproger.conferencetelegrambot.action.bus.dto.MakeQuestionUserAction;
import com.jproger.conferencetelegrambot.action.bus.dto.ShareContactUserAction;
import com.jproger.conferencetelegrambot.action.bus.dto.StartUserAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Optional;

import static com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType.TELEGRAM;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final ActionBus actionBus;
    private final TelegramBotProperties telegramBotProperties;

    public TelegramBot(ActionBus actionBus,
                       TelegramBotProperties telegramBotProperties,
                       DefaultBotOptions options) {
        super(options);

        this.actionBus = actionBus;
        this.telegramBotProperties = telegramBotProperties;
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getName();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Action action = null;

        if (isStartCommand(update)) {
            action = createStartUserAction(update);
        } else if (isShareContactCommand(update)) {
            action = createShareContactUserAction(update);
        } else if(isHasText(update)) {
            action = createMakeQuestionUserAction(update);
        }

        Optional.ofNullable(action)
                .ifPresent(actionBus::sendAction);
    }

    private boolean isStartCommand(Update update) {
        Message message = update.getMessage();

        return message.hasText() && message.getText().toLowerCase().startsWith("/start");
    }

    private boolean isShareContactCommand(Update update) {
        Message message = update.getMessage();

        return message.hasContact();
    }

    private boolean isHasText(Update update) {
        return update.getMessage().hasText();
    }

    private Action createStartUserAction(Update update) {
        Message message = update.getMessage();
        String channelUserId = message.getChatId().toString();
        String topic = Arrays.stream(message.getText().split(" "))
                .skip(1)                            // убираем строку "/start" из потока
                .filter(StringUtils::isNoneBlank)   // фильтруем от пустых строк
                .findFirst().orElse(null);

        return new StartUserAction(TELEGRAM, channelUserId, topic);
    }

    private Action createShareContactUserAction(Update update) {
        Message message = update.getMessage();
        String channelUserId = message.getChatId().toString();
        String phoneNumber = message.getContact().getPhoneNumber();
        String lastName = message.getFrom().getLastName();
        String firstName = message.getFrom().getFirstName();
        String middleName = "";

        return new ShareContactUserAction(TELEGRAM, channelUserId, lastName, firstName, middleName, phoneNumber);
    }

    private Action createMakeQuestionUserAction(Update update) {
        Message message = update.getMessage();
        String channelUserId = message.getChatId().toString();
        String text = message.getText();

        return new MakeQuestionUserAction(TELEGRAM, channelUserId, text);
    }
}
