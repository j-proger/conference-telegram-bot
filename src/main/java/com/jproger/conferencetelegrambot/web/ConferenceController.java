package com.jproger.conferencetelegrambot.web;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.api.TopicAPI;
import com.jproger.conferencetelegrambot.entities.Contact;
import com.jproger.conferencetelegrambot.entities.Question;
import com.jproger.conferencetelegrambot.entities.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ConferenceController {

    private final ContactAPI contactApi;
    private final QuestionAPI questionApi;
    private final TopicAPI topicApi;

    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Topic> topics() {
        return topicApi.getTopics();
    }

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Question> questions() {
        return questionApi.getQuestions();
    }

    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<Contact> contacts() {
        return contactApi.getContacts();
    }
}
