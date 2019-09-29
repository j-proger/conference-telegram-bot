package com.jproger.conferencetelegrambot.users.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Пользователь")
public class UserDto {
    @ApiModelProperty(value = "Идентификатор", example = "1")
    private Long id;

    @ApiModelProperty("Фамилия")
    private String lastName;

    @ApiModelProperty("Имя")
    private String firstName;

    @ApiModelProperty("Номер телефона")
    private String phoneNumber;
}
