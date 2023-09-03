package com.example.servicehub.support.file;

import org.springframework.web.multipart.MultipartFile;

import com.example.servicehub.dto.common.MultipartFileForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileTransfer<T extends MultipartFileForm> {

	private final LogoManager logoManager;

	public void transferLogoFileToStoreName(final T fileForm) {
		final MultipartFile file = fileForm.getFile();
		fileForm.setStoreName(logoManager.tryToRestore(file));
	}
}
