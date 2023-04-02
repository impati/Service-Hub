package com.example.servicehub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonMaker {
    private StringBuilder stringBuilder = new StringBuilder();

    public String make(Map<String, String> attributes) {
        stringBuilder.append("{").append(getNewLine());
        makeJsonString(attributes);
        stringBuilder.append("}").append(getNewLine());
        return stringBuilder.toString();
    }

    private void makeJsonString(Map<String, String> attributes) {
        List<String> jsonFields = new ArrayList<>(attributes.keySet());
        for (int i = 0; i < jsonFields.size(); i++) {
            String key = jsonFields.get(i);
            String value = attributes.get(key);
            setKeyValue(key, value, i, jsonFields.size());
        }
    }

    private void setKeyValue(String key, String value, int index, int size) {
        set(key);
        addColon();
        set(value);
        addCommaOrNothing(index, size);
        addNewLine();
    }

    private void addCommaOrNothing(int index, int size) {
        if (isNeedComma(index, size)) {
            stringBuilder.append(",");
        }
    }

    private boolean isNeedComma(int index, int size) {
        if (index + 1 == size) return false;
        return true;
    }

    private void addColon() {
        stringBuilder.append(" : ");
    }

    private void set(String keyOrValue) {
        stringBuilder.append(getQuotationMarks());
        stringBuilder.append(keyOrValue);
        stringBuilder.append(getQuotationMarks());
    }

    private void addNewLine() {
        stringBuilder.append("\n");
    }

    private String getQuotationMarks() {
        return "\"";
    }

    private String getNewLine() {
        return "\n";
    }

}
