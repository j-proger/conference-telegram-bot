package com.jproger.conferencetelegrambot.channels.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions.ProxyType;

@Data
@Component
@ConfigurationProperties(prefix = "channels.telegram.bot")
public class TelegramBotProperties {
    private String name;
    private String token;
    private String proxyHost;
    private int proxyPort;
    private ProxyType proxyType;
}
