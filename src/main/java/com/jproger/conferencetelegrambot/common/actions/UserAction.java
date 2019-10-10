package com.jproger.conferencetelegrambot.common.actions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public abstract class UserAction extends Action {
    public UserAction(ChannelType channel, String channelUserId) {
        super(channel, channelUserId);
    }
}
