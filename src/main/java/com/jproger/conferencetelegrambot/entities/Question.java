package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class Question {

    @Getter @Setter
    private String question;

    @Getter @Setter
    private Contact author;
}
