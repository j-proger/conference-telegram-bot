package com.jproger.conferencetelegrambot.users.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phoneNumber;
}
