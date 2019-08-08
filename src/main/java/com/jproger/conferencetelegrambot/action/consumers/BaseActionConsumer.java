package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.ActionConsumer;
import com.jproger.conferencetelegrambot.action.bus.dto.Action;

import javax.annotation.PostConstruct;

public abstract class BaseActionConsumer<T extends Action> implements ActionConsumer {
    private final Class<T> tClass;
    protected final ActionBus actionBus;

    protected BaseActionConsumer(Class<T> tClass, ActionBus actionBus) {
        this.tClass = tClass;
        this.actionBus = actionBus;
    }

    @PostConstruct
    public void registerInBus() {
        actionBus.registerConsumer(this);
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return tClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(Action action) {
        acceptTAction((T) action);
    }

    protected abstract void acceptTAction(T action);
}
