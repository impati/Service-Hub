package com.example.servicehub.util;

import static java.util.stream.Collectors.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FormDataEncoder {

	public <T> String encode(final Object obj, final Class<T> clazz) {
		final ObjectMapper objectMapper = new ObjectMapper();
		final Map<String, Object> fieldMap = objectMapper.convertValue(
			obj,
			new TypeReference<>() {
			});

		final MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

		valueMap.setAll(addParamNoCollection(fieldMap, clazz));
		valueMap.addAll(addParamCollectionType(fieldMap, clazz));

		if (valueMap.isEmpty()) {
			return "";
		}

		return UriComponentsBuilder.newInstance()
			.queryParams(valueMap)
			.encode()
			.build()
			.getQuery();
	}

	private <T> Map<String, String> addParamNoCollection(final Map<String, Object> fieldMap, final Class<T> clazz) {
		return Arrays.stream(clazz.getDeclaredFields())
			.filter(field -> !Collection.class.isAssignableFrom(field.getType()))
			.map(Field::getName)
			.filter(fieldMap::containsKey)
			.filter(key -> fieldMap.get(key) != null)
			.collect(toMap(key -> key, key -> String.valueOf(fieldMap.get(key))));
	}

	private <T> MultiValueMap<String, String> addParamCollectionType(
		final Map<String, Object> fieldMap,
		final Class<T> clazz
	) {
		final Map<String, List<String>> collect = Arrays.stream(clazz.getDeclaredFields())
			.filter(field -> Collection.class.isAssignableFrom(field.getType()))
			.map(Field::getName)
			.filter(key -> fieldMap.get(key) != null)
			.collect(toMap(name -> name, name -> ((List<String>)fieldMap.get(name))));

		final MultiValueMap<String, String> container = new LinkedMultiValueMap<>();

		for (var key : collect.keySet()) {
			addKeyValue(collect, container, key);
		}
		return container;
	}

	private static void addKeyValue(
		final Map<String, List<String>> collect,
		final MultiValueMap<String, String> container,
		final String key
	) {
		for (var value : collect.get(key)) {
			container.add(key, value);
		}
	}
}
