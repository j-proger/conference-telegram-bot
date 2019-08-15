package com.jproger.conferencetelegrambot.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@ApiModel("Страница данных")
public class PageResponseDto<T> {
    @ApiModelProperty(value = "Всего элементов", example = "135")
    private long totalElements;

    @ApiModelProperty(value = "Всего страниц", example = "99")
    private int totalPages;

    @ApiModelProperty(value = "Загруженная страница", example = "2")
    private int currentPage;

    @ApiModelProperty(value = "Элементы страницы")
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
