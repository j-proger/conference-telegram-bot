package com.jproger.conferencetelegrambot.web;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.api.QuestionAPI;
import com.jproger.conferencetelegrambot.entities.Contact;
import com.jproger.conferencetelegrambot.entities.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;

@ExtendWith({SpringExtension.class})
@WebMvcTest({ConferenceController.class})
@Import(TestConfig.class)
class ConferenceControllerTest {

    @MockBean
    private ContactAPI contactApi;

    @MockBean
    private QuestionAPI questionApi;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_topics() throws Exception { // @formatter:off
        mockMvc
            .perform(get("/topics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].key", containsInAnyOrder("TELEGRAM_BOT_IN_8_HOURS", "SPRING_BOOT_IN_PRODUCTION")))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Телеграм-бот за 8 часов", "Spring-Boot на проде")));
    } // @formatter:on

    @Test
    void should_return_contacts() throws Exception { // @formatter:off

        // setup:
        when(contactApi.getContacts())
                .thenReturn(
                        new HashSet<>(
                                Arrays.asList(
                                Contact.builder()
                                        .name("Bruce Lee")
                                        .phoneNumber("+74951111111")
                                        .telegramID("bruce_lee")
                                        .build(),
                                Contact.builder()
                                        .name("Chuck Norris")
                                        .phoneNumber("+74959999999")
                                        .telegramID("chuck_norris")
                                        .build())
                        )
                );

        mockMvc
            .perform(get("/contacts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Bruce Lee", "Chuck Norris")))
                .andExpect(jsonPath("$[*].phoneNumber", containsInAnyOrder("+74951111111", "+74959999999")))
                .andExpect(jsonPath("$[*].telegramID", containsInAnyOrder("bruce_lee", "chuck_norris")));
    } // @formatter:on

    @Test
    void should_return_questions() throws Exception { // @formatter:off

        // setup:
        when(questionApi.getQuestions())
                .thenReturn(
                        new HashSet<>(
                                Arrays.asList(
                                Question.builder()
                                        .question("Question from Bruce Lee.")
                                        .author(
                                                Contact.builder()
                                                        .name("Bruce Lee")
                                                        .phoneNumber("+74951111111")
                                                        .telegramID("bruce_lee")
                                                        .build()
                                        )
                                        .topicKey("TELEGRAM_BOT_IN_8_HOURS")
                                        .build(),
                                Question.builder()
                                        .question("Question from Chuck Norris.")
                                        .author(
                                                Contact.builder()
                                                        .name("Chuck Norris")
                                                        .phoneNumber("+74959999999")
                                                        .telegramID("chuck_norris")
                                                        .build()
                                        )
                                        .topicKey("SPRING_BOOT_IN_PRODUCTION")
                                        .build()
                        ))
                );

        mockMvc
                .perform(get("/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].question", containsInAnyOrder("Question from Bruce Lee.", "Question from Chuck Norris.")))
                .andExpect(jsonPath("$[*].topicKey", containsInAnyOrder("SPRING_BOOT_IN_PRODUCTION", "TELEGRAM_BOT_IN_8_HOURS")))
                .andExpect(jsonPath("$[*].author.name", containsInAnyOrder("Bruce Lee", "Chuck Norris")))
                .andExpect(jsonPath("$[*].author.phoneNumber", containsInAnyOrder("+74951111111", "+74959999999")))
                .andExpect(jsonPath("$[*].author.telegramID", containsInAnyOrder("bruce_lee", "chuck_norris")));
    } // @formatter:on
}