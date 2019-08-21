package com.jproger.conferencetelegrambot.channels.telegram;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Optional;

import static com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType.TELEGRAM;
import static com.jproger.conferencetelegrambot.channels.telegram.TelegramCommandConstants.START_COMMAND;
import static com.jproger.conferencetelegrambot.channels.telegram.TelegramCommandConstants.STATUS_REQUEST_COMMAND;

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
        } else if(isStatusRequestCommand(update)) {
            action = createStatusRequestUserAction(update);
        } else if(isHasText(update)) {
            action = createDefaultUserAction(update);
        }

        Optional.ofNullable(action)
                .ifPresent(actionBus::sendAction);
    }

    private boolean isStartCommand(Update update) {
        Message message = update.getMessage();

        return message.hasText() && message.getText().toLowerCase().startsWith(START_COMMAND);
    }

    private boolean isShareContactCommand(Update update) {
        Message message = update.getMessage();

        return message.hasContact();
    }

    private boolean isStatusRequestCommand(Update update) {
        Message message = update.getMessage();

        return message.hasText() && message.getText().toLowerCase().startsWith(STATUS_REQUEST_COMMAND);
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

        return new InitWorkflowUserAction(TELEGRAM, channelUserId, topic);
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

    private Action createStatusRequestUserAction(Update update) {
        String channelUserId = update.getMessage().getChatId().toString();

        return new StatusRequestUserAction(TELEGRAM, channelUserId);
    }

    private Action createDefaultUserAction(Update update) {
        Message message = update.getMessage();
        String channelUserId = message.getChatId().toString();
        String text = message.getText();

        return new DefaultUserAction(TELEGRAM, channelUserId, text);
    }
}
