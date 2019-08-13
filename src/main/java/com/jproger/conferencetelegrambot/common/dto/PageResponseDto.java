package com.jproger.conferencetelegrambot.common.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PageResponseDto<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private List<T> elements;

    public static <T> PageResponseDto<T> of(Page<T> page) {
        return PageResponseDto.<T>builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .elements(page.getContent())
                .build();
    }
}
