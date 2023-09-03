package com.example.servicehub.support.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.example.servicehub.exception.file.FileStoreException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LogoManager extends AbstractFileManager {

	@Value("${logo.dir}")
	private String logoDir;

	private final ImageResizer imageResizer;

	@Override
	public String getFullPath(final String logoName) {
		return logoDir + logoName;
	}

	@Override
	public String tryToRestore(final MultipartFile logo) {
		try {
			return imageResizer.resizeImageAndSave(restore(logo));
		} catch (IOException e) {
			throw new FileStoreException(e.getMessage() + "[" + logo + "] 로고 이미지 저장 실패 ");
		}
	}

	public String download(final String logoUrl) {
		final String storeName = createUniqueName() + ImageExtension.extractExtension(logoUrl);
		try (InputStream in = new URL(logoUrl).openStream()) {
			final Path imagePath = Paths.get(getFullPath(storeName));
			Files.copy(in, imagePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imageResizer.resizeImageAndSave(storeName);
	}
}
