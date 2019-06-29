package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class Contact {
    private String name;

    private String phoneNumber;

    private String telegramID;
}
