package com.jproger.conferencetelegrambot.action.bus;

import com.jproger.conferencetelegrambot.common.actions.Action;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ActionBus {
    private List<ActionConsumer> consumers = new LinkedList<>();

    public void registerConsumer(ActionConsumer consumer) {
        consumers.add(consumer);
    }

    public void sendAction(Action action) {
        Class<? extends Action> actionClass = action.getClass();

        consumers.stream()
                .filter(consumer -> consumer.getActionClass().equals(actionClass))
                .forEach(consumer -> consumer.accept(action));
    }
}
