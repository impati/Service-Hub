package com.example.servicehub.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class FormDataEncoder {

    public <T> String encode(Object obj , Class<T> clazz) {

        Map<String, String> fieldMap =
                new ObjectMapper().convertValue(obj, new TypeReference<>() {});

        Map<String, String> newFieldMap = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldMap::containsKey)
                .filter(key -> fieldMap.get(key) != null)
                .collect(toMap(key -> key, fieldMap::get));

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

        valueMap.setAll(newFieldMap);

        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }


}
