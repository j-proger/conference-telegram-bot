package com.jproger.conferencetelegrambot.telegram;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.api.QuestionAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.DefaultBotOptions.ProxyType;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Configuration
@Profile("matvey-local")
@RequiredArgsConstructor
public class TelegramConfiguration {
    private static final String BOT_TOKEN = "868656591:AAEN_gOc2GQa4zo5Gp4KWPq1Rt_XgZBuSgY";
    private static final String BOT_NAME = "ConferenceRequesterBot";
    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 9150;
    private static final ProxyType PROXY_TYPE = ProxyType.SOCKS5;

    private final ContactAPI contactAPI;
    private final QuestionAPI questionAPI;

    @PostConstruct
    public void initializeBot() throws TelegramApiRequestException {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        options.setProxyType(PROXY_TYPE);
        options.setProxyPort(PROXY_PORT);
        options.setProxyHost(PROXY_HOST);

        botsApi.registerBot(new TelegramBot(contactAPI, questionAPI, BOT_NAME, BOT_TOKEN, options));
    }
}
