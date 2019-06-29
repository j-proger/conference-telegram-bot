package com.jproger.conferencetelegrambot.web;

import com.jproger.conferencetelegrambot.entities.Contact;
import com.jproger.conferencetelegrambot.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConferenceController {

    private final ConferenceService service;

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Question> questions() {
        return service.questions();
    }

    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Contact> contacts() {
        return service.contacts();
    }
}
