package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;

@Getter
public class ShareContactUserAction extends UserAction {
    private String lastName;
    private String firstName;
    private String middleName;
    private String phoneNumber;

    public ShareContactUserAction(ChannelType channel,
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
