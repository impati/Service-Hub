package com.example.servicehub.util;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonMakerTest {

	@Test
	@DisplayName("Json Maker Test")
	void JsonMakerTest() {
		final JsonMaker jsonMaker = new JsonMaker();

		final Map<String, String> attributes = new HashMap<>();

		attributes.put("id", "99");
		attributes.put("name", "test");

		assertThat(jsonMaker.make(attributes)).isEqualTo(result());

	}

	private String result() {
		return "{\n" +
			"\"name\" : \"test\",\n" +
			"\"id\" : \"99\"\n" +
			"}\n";
	}
}
