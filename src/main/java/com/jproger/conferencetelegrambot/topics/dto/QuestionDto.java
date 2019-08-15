package com.jproger.conferencetelegrambot.topics.dto;

import com.jproger.conferencetelegrambot.users.dto.UserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Вопрос")
public class QuestionDto {
    @ApiModelProperty(value = "Идентификатор", example = "1")
    private Long id;

    @ApiModelProperty(value = "Идентификатор доклада", example = "1")
    private Long topicId;

    @ApiModelProperty("Пользователь создавший вопрос")
    private UserDto user;

    @ApiModelProperty("Текст вопроса")
    private String description;
}
