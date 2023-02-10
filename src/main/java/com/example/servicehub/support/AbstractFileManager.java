package com.example.servicehub.support;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractFileManager implements FileManager{

    public  final static String DEFAULT = "default.png";
    private final static List<String> imageExtension = new ArrayList<>(List.of("png","bmp","rle","dib","jpeg","jpg","gif","tif","tiff","raw"));

    protected String createUniqueName(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    protected String extractExtension(String originFileName){
        return "." + imageExtension
                .stream()
                .filter(originFileName.toLowerCase()::contains)
                .findFirst().orElseThrow(()->new IllegalStateException("지원하지 않는 파일형식입니다"));
    }

    protected boolean isContainFile(MultipartFile profile){
        if(profile == null) return false;
        if(profile.isEmpty()) return false;
        return true;
    }

    protected String restore(MultipartFile file) throws IOException {
        if(isContainFile(file)){
            String storeName = createUniqueName() + extractExtension(file.getOriginalFilename());
            file.transferTo(new File(getFullPath(storeName)));
            return storeName;
        }
        return DEFAULT;
    }

}
