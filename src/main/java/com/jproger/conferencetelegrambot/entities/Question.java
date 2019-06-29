package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class Question {
    private String question;
    private Contact contact;
}
