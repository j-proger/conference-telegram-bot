package com.jproger.conferencetelegrambot.action.bus;

import com.jproger.conferencetelegrambot.core.operations.dto.Operation;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ActionBus {
    private List<ActionConsumer> consumers = new LinkedList<>();

    public void registerConsumer(ActionConsumer consumer) {
        consumers.add(consumer);
    }

    public void sendAction(Operation operation) {
        Class<? extends Operation> actionClass = operation.getClass();

        consumers.stream()
                .filter(consumer -> consumer.getActionClass().equals(actionClass))
                .forEach(consumer -> consumer.accept(operation));
    }
}
