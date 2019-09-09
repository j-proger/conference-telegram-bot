package com.jproger.conferencetelegrambot.action.bus;

import com.jproger.conferencetelegrambot.core.operations.dto.Operation;

import java.util.function.Consumer;

public interface ActionConsumer extends Consumer<Operation> {
    Class<? extends Operation> getActionClass();
}
