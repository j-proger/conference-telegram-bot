package com.jproger.conferencetelegrambot.telegram;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWorkflow {
    enum State {
        SHARE_CONTACT,
        ASK_QUESTION
    }

    private Integer id;
    private Long chatId;
    private State state;
}
