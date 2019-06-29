package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Question {
    private String question;
    private Contact contact;
}
