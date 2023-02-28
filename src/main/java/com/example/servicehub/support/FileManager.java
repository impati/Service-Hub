package com.example.servicehub.support;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

    String getFullPath(String profileName);

    String tryToRestore(MultipartFile profile);
}
