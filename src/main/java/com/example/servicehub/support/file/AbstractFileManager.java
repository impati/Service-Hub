package com.example.servicehub.support.file;

import static java.util.Objects.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractFileManager implements FileManager {

	public static final String DEFAULT = "default.png";

	protected String restore(final MultipartFile file) throws IOException {
		if (isContainFile(file)) {
			final String storeName =
				createUniqueName() + ImageExtension.extractExtension(requireNonNull(file.getOriginalFilename()));
			file.transferTo(new File(getFullPath(storeName)));
			return storeName;
		}

		return DEFAULT;
	}

	protected String createUniqueName() {
		return UUID.randomUUID().toString();
	}

	protected boolean isContainFile(final MultipartFile file) {
		if (file == null) {
			return false;
		}

		return !file.isEmpty();
	}
}
