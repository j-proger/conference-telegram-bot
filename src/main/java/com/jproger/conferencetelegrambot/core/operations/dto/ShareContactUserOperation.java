package com.jproger.conferencetelegrambot.core.operations.dto;

import lombok.Getter;

@Getter
public class ShareContactUserOperation extends UserOperation {
    private String lastName;
    private String firstName;
    private String middleName;
    private String phoneNumber;

    public ShareContactUserOperation(ChannelType channel,
                                     String externalUserId,
                                     String lastName,
                                     String firstName,
                                     String middleName,
                                     String phoneNumber) {
        super(channel, externalUserId);

        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
    }
}
