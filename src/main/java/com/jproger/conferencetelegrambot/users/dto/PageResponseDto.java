package com.jproger.conferencetelegrambot.users.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDto<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private List<T> elements;
}
