package com.example.servicehub.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClickServiceDtoTest {

    @Test
    @DisplayName("Categories 를 String 변환 테스트")
    public void given_when_then() throws Exception{
        // given
        List<String>  categories = List.of("IT","BLOG");
        // when
        ClickServiceDto clickServiceDto =
                new ClickServiceDto(null, null, null, null, null, null, categories,false);
        // then
        assertThat(clickServiceDto.getCategories())
                .isEqualTo("IT BLOG");
    }

}