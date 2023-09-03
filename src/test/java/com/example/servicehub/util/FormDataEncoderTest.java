package com.example.servicehub.util;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormDataEncoderTest {

	@Test
	@DisplayName("FormDataEncoder - container Collection Test")
	void existCollection() {
		// given
		final String username = "test";
		final List<String> categories = List.of("IT", "BLOG");
		final Pair pair = new Pair(username, categories);
		final FormDataEncoder formDataEncoder = new FormDataEncoder();
		final String encode = formDataEncoder.encode(pair, Pair.class);

		assertThat(encode).isEqualTo(
			"username=" + username +
				"&categories=" + "IT" +
				"&categories=" + "BLOG"
		);
	}

	@Test
	@DisplayName("컬렉션 필드 네임 가져오기")
	void getCollectionFieldNames() {

		final List<String> collectionFieldNames = Arrays.stream(Pair.class.getDeclaredFields())
			.filter(field -> Collection.class.isAssignableFrom(field.getType()))
			.map(Field::getName)
			.collect(toList());

		assertThat(collectionFieldNames)
			.contains("categories");
	}

	@Test
	@DisplayName("리플렉션 타입 검사")
	void reflection() {
		final List<String> container = new ArrayList<>();

		assertThat(Collection.class.isAssignableFrom(container.getClass()))
			.isTrue();
	}

	static class Pair {
		String username;
		List<String> categories;

		public Pair(final String username, final List<String> categories) {
			this.username = username;
			this.categories = categories;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public List<String> getCategories() {
			return categories;
		}

		public void setCategories(List<String> categories) {
			this.categories = categories;
		}
	}
}
