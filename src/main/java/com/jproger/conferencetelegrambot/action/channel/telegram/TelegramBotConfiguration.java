package com.jproger.conferencetelegrambot.action.channel.telegram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class TelegramBotConfiguration {

    public TelegramBotConfiguration() {
        ApiContextInitializer.init();
    }

    @Bean
    public DefaultBotOptions botOptions(TelegramBotProperties properties) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        options.setProxyType(properties.getProxyType());
        options.setProxyPort(properties.getProxyPort());
        options.setProxyHost(properties.getProxyHost());

        return options;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiRequestException {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        botsApi.registerBot(bot);

        return botsApi;
    }
}
