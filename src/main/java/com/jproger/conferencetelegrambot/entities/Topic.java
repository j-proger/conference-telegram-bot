package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Topic {
    private String key;
    private String name;
}
