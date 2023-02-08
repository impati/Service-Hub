package com.example.servicehub.support;

import com.example.servicehub.exception.FileStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class ProfileManager {

    public static final String DEFAULT = "default.png";
    @Value("${profile.dir}")
    private String profileDir;

    public String getFullPath (String profileName){
        return profileDir + profileName;
    }

    public String tryToRestore(MultipartFile profile){
        try{
            return restore(profile);
        }catch (Exception e){
            throw new FileStoreException("[" + profile + "] 프로필 이미지 저장 실패 ");
        }
    }

    public String restore(MultipartFile profile) throws IOException {
        if(isContainFile(profile)){
            String storeName = createUniqueName() + extractExtension(profile.getOriginalFilename());
            profile.transferTo(new File(getFullPath(storeName)));
            return storeName;
        }
        return DEFAULT;
    }

    private boolean isContainFile(MultipartFile profile){
        if(profile == null) return false;
        if(profile.isEmpty()) return false;
        return true;
    }

    private String extractExtension(String originFileName){
        String[] splitStringByPoint = originFileName.split("\\.");
        return "." + splitStringByPoint[splitStringByPoint.length - 1];
    }

    private String createUniqueName(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
