package com.jproger.conferencetelegrambot;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Configuration
public class TelegramBotConfiguration {
    private static String BOT_NAME = "ConferenceRequesterBot";
    private static String BOT_TOKEN = "868656591:AAEN_gOc2GQa4zo5Gp4KWPq1Rt_XgZBuSgY" /* your bot's token here */;

    private static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private static Integer PROXY_PORT = 9150 /* proxy port */;

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        options.setProxyPort(PROXY_PORT);
        options.setProxyHost(PROXY_HOST);

        botsApi.registerBot(new TestBot(options));
    }
}
