package com.jproger.conferencetelegrambot.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String phoneNumber;
}
