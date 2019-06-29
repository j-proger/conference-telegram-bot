package com.jproger.conferencetelegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
    @GetMapping("/questions")
    public String test() {
        return "Hello World!";
    }
}
