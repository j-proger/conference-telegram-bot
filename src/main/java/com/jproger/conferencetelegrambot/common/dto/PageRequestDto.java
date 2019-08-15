package com.jproger.conferencetelegrambot.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel("Запрос страницы данных")
public class PageRequestDto {
    @Min(0)
    @ApiModelProperty(value = "Номер страницы", example = "0")
    private int page;

    @Min(1)
    @Max(100)
    @ApiModelProperty(value = "Размер страницы", example = "10")
    private int size;
}
