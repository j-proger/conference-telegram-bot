package com.jproger.conferencetelegrambot.web;

import com.jproger.conferencetelegrambot.entities.Contact;
import com.jproger.conferencetelegrambot.entities.Question;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ConferenceService {

    private AtomicLong sequence = new AtomicLong();
    private Map<Long, Question> questions = new ConcurrentHashMap<>();
    private Map<Long, Contact> contacts = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {

        Contact contact = Contact.builder()
                .name("Test Name")
                .phone("Test Phone")
                .build();

        Question question = Question.builder()
                .question("Question test")
                .contact(contact)
                .build();

        contacts.put(sequence.incrementAndGet(), contact);
        questions.put(sequence.incrementAndGet(), question);
    }

    @SuppressWarnings("WeakerAccess")
    public List<Question> questions() {
        return new ArrayList<>(questions.values());
    }

    @SuppressWarnings("WeakerAccess")
    public List<Contact> contacts() {
        return new ArrayList<>(contacts.values());
    }

    @SuppressWarnings("unused")
    public void addQuestion(Question question) {
        questions.put(sequence.incrementAndGet(), question);
    }

    @SuppressWarnings("unused")
    public void addContact(Contact contact) {
        contacts.put(sequence.incrementAndGet(), contact);
    }
}
