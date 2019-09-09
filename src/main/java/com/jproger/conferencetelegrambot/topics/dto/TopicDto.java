package com.jproger.conferencetelegrambot.topics.dto;

import com.jproger.conferencetelegrambot.topics.entities.Topic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Доклад")
public class TopicDto {
    @ApiModelProperty(value = "Идентификатор доклада", example = "1")
    private Long id;

    @ApiModelProperty("Наименование доклада")
    private String name;

    @ApiModelProperty("Ключ доклада (он должен быть указан при создании вопроса из телеграмма)")
    private String key;

    @ApiModelProperty("Статус доклада")
    private Topic.Status status;
}
