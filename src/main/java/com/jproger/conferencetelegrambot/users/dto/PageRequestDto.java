package com.jproger.conferencetelegrambot.users.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class PageRequestDto {
    @Min(0)
    private int page;

    @Min(1)
    @Max(100)
    private int size;
}
