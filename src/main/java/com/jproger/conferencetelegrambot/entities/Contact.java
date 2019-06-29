package com.jproger.conferencetelegrambot.entities;

import lombok.*;

@Data
@Builder
public class Contact {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String telegramID;


}
