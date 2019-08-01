package com.jproger.conferencetelegrambot.action.bus;

import com.jproger.conferencetelegrambot.action.bus.dto.Action;

import java.util.function.Consumer;

public interface ActionConsumer extends Consumer<Action> {
    Class<? extends Action> getActionClass();
}
