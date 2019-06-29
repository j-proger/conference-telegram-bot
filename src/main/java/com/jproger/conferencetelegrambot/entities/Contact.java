package com.jproger.conferencetelegrambot.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Accessors(chain = true)
public class Contact {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String telegramID;


}
