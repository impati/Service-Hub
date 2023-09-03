package com.example.servicehub.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonMaker {

	private final StringBuilder stringBuilder = new StringBuilder();

	public String make(final Map<String, String> attributes) {
		stringBuilder.append("{").append(getNewLine());
		makeJsonString(attributes);
		stringBuilder.append("}").append(getNewLine());
		return stringBuilder.toString();
	}

	private void makeJsonString(final Map<String, String> attributes) {
		final List<String> jsonFields = new ArrayList<>(attributes.keySet());

		for (int i = 0; i < jsonFields.size(); i++) {
			final String key = jsonFields.get(i);
			final String value = attributes.get(key);
			setKeyValue(key, value, i, jsonFields.size());
		}
	}

	private void setKeyValue(
		final String key,
		final String value,
		final int index,
		final int size
	) {
		set(key);
		addColon();
		set(value);
		addCommaOrNothing(index, size);
		addNewLine();
	}

	private void addCommaOrNothing(final int index, final int size) {
		if (isNeedComma(index, size)) {
			stringBuilder.append(",");
		}
	}

	private boolean isNeedComma(final int index, final int size) {
		return index + 1 != size;
	}

	private void addColon() {
		stringBuilder.append(" : ");
	}

	private void set(final String keyOrValue) {
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
