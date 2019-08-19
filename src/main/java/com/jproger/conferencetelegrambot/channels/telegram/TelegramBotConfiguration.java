package com.jproger.conferencetelegrambot.channels.telegram;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.channels.telegram.TelegramBotProperties.ProxyProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
        ProxyProperties proxy = properties.getProxy();
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        if (proxy.isEnabled()) {
            options.setProxyType(proxy.getType());
            options.setProxyPort(proxy.getPort());
            options.setProxyHost(proxy.getHost());
        }

        return options;
    }

    @Bean
    public TelegramBot telegramBot(ActionBus bus, TelegramBotProperties properties, DefaultBotOptions options) {
        return new TelegramBot(bus, properties, options);
    }

    @Bean
    @ConditionalOnProperty(name = "channels.telegram.bot.enabled", havingValue = "true")
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiRequestException {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        botsApi.registerBot(bot);

        return botsApi;
    }
}
