package com.example.servicehub.support.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

	String getFullPath(final String profileName);

	String tryToRestore(final MultipartFile profile);
}
