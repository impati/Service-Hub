package com.example.servicehub.support;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileManager {

    String getFullPath(String profileName);

    String tryToRestore(MultipartFile profile);
}
