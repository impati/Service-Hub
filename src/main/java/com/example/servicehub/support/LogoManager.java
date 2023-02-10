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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class LogoManager {
    @Value("${logo.dir}")
    private String logoDir;
    private final static List<String> imageExtension = new ArrayList<>(List.of("png","bmp","rle","dib","jpeg","jpg","gif","tif","tiff","raw"));

    public String getFullPath (String logoName){
        return logoDir + logoName;
    }

    public String tryToRestore(MultipartFile logo){
        try{
            return restore(logo);
        }catch (Exception e){
            throw new FileStoreException("[" + logo + "] 로고 이미지 저장 실패 ");
        }
    }

    public String restore(MultipartFile logo) throws IOException {
        if(!logo.isEmpty()){
            String storeName = createUniqueName() + extractExtension(logo.getOriginalFilename());
            logo.transferTo(new File(getFullPath(storeName)));
            return storeName;
        }
        return null;
    }

    public String download(String logoUrl){
        String storeName = createUniqueName() + extractExtension(logoUrl);
        try(InputStream in = new URL(logoUrl).openStream()){
            Path imagePath = Paths.get(getFullPath(storeName));
            Files.copy(in, imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return storeName;
    }


    private String extractExtension(String originFileName){
        return "." + imageExtension
                .stream()
                .filter(originFileName.toLowerCase()::contains)
                .findFirst().orElseThrow(()->new IllegalStateException("지원하지 않는 파일형식입니다"));
    }


    private String createUniqueName(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}