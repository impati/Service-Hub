package com.example.servicehub.support;

import com.example.servicehub.dto.MultipartFileForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class FileTransfer<T extends MultipartFileForm> {

    private final LogoManager logoManager;
    private final ProfileManager profileManager;

    public T transferLogoFileToStoreName(T fileForm) {
        MultipartFile file = fileForm.getFile();
        fileForm.setStoreName(logoManager.tryToRestore(file));
        return fileForm;
    }
}
