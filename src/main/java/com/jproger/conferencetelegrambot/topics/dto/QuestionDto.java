package com.jproger.conferencetelegrambot.topics.dto;

import com.jproger.conferencetelegrambot.users.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDto {
    private Long id;
    private Long topicId;
    private UserDto user;
    private String description;
}
