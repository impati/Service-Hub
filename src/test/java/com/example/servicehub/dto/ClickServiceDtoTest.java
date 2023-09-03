package com.example.servicehub.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.servicehub.dto.services.ClickServiceDto;

class ClickServiceDtoTest {

	@Test
	@DisplayName("Categories 를 String 변환 테스트")
	void given_when_then() {
		// given
		final List<String> categories = List.of("IT", "BLOG");
		// when
		final ClickServiceDto clickServiceDto =
			new ClickServiceDto(null, null, null, null, null, null, categories, false);
		// then
		assertThat(clickServiceDto.getCategories())
			.isEqualTo("IT BLOG");
	}
}
