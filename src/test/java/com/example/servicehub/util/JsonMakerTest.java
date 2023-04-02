package com.example.servicehub.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class JsonMakerTest {


    @Test
    @DisplayName("Json Maker Test")
    public void JsonMakerTest() throws Exception {
        JsonMaker jsonMaker = new JsonMaker();

        Map<String, String> attributes = new HashMap<>();

        attributes.put("id", "99");
        attributes.put("name", "test");

        Assertions.assertThat(jsonMaker.make(attributes)).isEqualTo(result());

    }

    private String result() {
        return "{\n" +
                "\"name\" : \"test\",\n" +
                "\"id\" : \"99\"\n" +
                "}\n";
    }
}