package com.example.servicehub.support.file;

import lombok.Getter;

@Getter
public enum FileType {

	logo("logo"), profile("profile");

	private final String type;

	FileType(final String type) {
		this.type = type;
	}
}

