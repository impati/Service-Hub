package com.example.servicehub.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class FormDataEncoder {

    public <T> String encode(Object obj , Class<T> clazz) {

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> fieldMap = objectMapper.convertValue(obj,new TypeReference<>(){});

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

        valueMap.setAll(addParamNoCollection(fieldMap,clazz));

        valueMap.addAll(addParamCollectionType(fieldMap,clazz));

        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }


    private <T> Map<String, String > addParamNoCollection(Map<String,Object> fieldMap ,Class<T> clazz){
        return  Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .filter(fieldMap::containsKey)
                .filter(key -> fieldMap.get(key) != null)
                .collect(toMap(key->key,key->String.valueOf(fieldMap.get(key))));
    }

    private <T> MultiValueMap<String,String> addParamCollectionType(Map<String,Object> fieldMap ,Class<T> clazz){
        Map<String, List<String>> collect = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .filter(key -> fieldMap.get(key) != null)
                .collect(toMap(name -> name, name -> ((List<String>) fieldMap.get(name))));

        MultiValueMap<String,String> container = new LinkedMultiValueMap<>();

        for (var key : collect.keySet()){
            for(var value : collect.get(key)) {
                container.add(key,value);
            }
        }
        return container;
    }





}
