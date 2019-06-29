package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private String name;
    private String phone;
}
