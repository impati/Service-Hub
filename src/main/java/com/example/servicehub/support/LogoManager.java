package com.example.servicehub.support;

import com.example.servicehub.exception.FileStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class LogoManager extends AbstractFileManager{

    @Value("${logo.dir}")
    private String logoDir;

    @Override
    public String getFullPath (String logoName){
        return logoDir + logoName;
    }

    @Override
    public String tryToRestore(MultipartFile logo){
        try{
            return restore(logo);
        }catch (Exception e){
            throw new FileStoreException(e.getMessage() + "[" + logo + "] 로고 이미지 저장 실패 ");
        }
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


}