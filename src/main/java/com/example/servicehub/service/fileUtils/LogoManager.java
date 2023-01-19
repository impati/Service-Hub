package com.example.servicehub.service.fileUtils;

import com.example.servicehub.exception.FileStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class LogoManager {
    @Value("${logo.dir}")
    private String logoDir;

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

    private String extractExtension(String originFileName){
        String[] splitStringByPoint = originFileName.split("\\.");
        return "." + splitStringByPoint[splitStringByPoint.length - 1];
    }

    private String createUniqueName(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
