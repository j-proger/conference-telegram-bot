package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class Question {

    @Getter @Setter
    private String question;

    @Getter @Setter
    private Contact author;
}
