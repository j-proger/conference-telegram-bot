package com.jproger.conferencetelegrambot.web;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.entities.Contact;
import com.jproger.conferencetelegrambot.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ConferenceController {

    private final ContactAPI contactApi;
    private final QuestionAPI questionApi;

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Question> questions() {
        return questionApi.getQuestions();
    }

    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Contact> contacts() {
        return contactApi.getContacts();
    }
}
