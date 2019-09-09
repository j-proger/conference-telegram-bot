package com.jproger.conferencetelegrambot.workflow.entities;

public enum Status {
    /**
     * Когда пользователь только пришел к нам
     */
    NEW,

    /**
     * Когда пользователь поделился контактами
     */
    REGISTERED,

    /**
     * Выбрал доклад и может задавать вопросы
     */
    COLLECT_QUESTIONS,

    /**
     * Сбор отзывов о работе бота
     */
    COLLECT_FEEDBACK
}
