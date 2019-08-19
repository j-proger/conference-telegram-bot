package com.jproger.conferencetelegrambot.channels.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions.ProxyType;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "channels.telegram.bot")
public class TelegramBotProperties {
    private boolean enabled;
    private String name;
    private String token;
    private ProxyProperties proxy;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyProperties {
        private String host;
        private int port;
        private ProxyType type;
        private boolean enabled;
    }
}
