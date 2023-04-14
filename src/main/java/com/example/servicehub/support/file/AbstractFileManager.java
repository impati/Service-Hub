package com.example.servicehub.support.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public abstract class AbstractFileManager implements FileManager {

    public final static String DEFAULT = "default.png";

    protected String restore(MultipartFile file) throws IOException {
        if (isContainFile(file)) {
            String storeName = createUniqueName() + ImageExtension.extractExtension(requireNonNull(file.getOriginalFilename()));
            file.transferTo(new File(getFullPath(storeName)));
            return storeName;
        }
        return DEFAULT;
    }

    protected String createUniqueName() {
        return UUID.randomUUID().toString();
    }

    protected boolean isContainFile(MultipartFile file) {
        if (file == null) return false;
        return !file.isEmpty();
    }

}
