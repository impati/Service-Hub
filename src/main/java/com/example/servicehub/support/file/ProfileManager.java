package com.example.servicehub.support.file;

import com.example.servicehub.exception.file.FileStoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
public class ProfileManager extends AbstractFileManager {

    @Value("${profile.dir}")
    private String profileDir;

    @Override
    public String getFullPath(String profileName) {
        return profileDir + profileName;
    }

    @Override
    public String tryToRestore(MultipartFile profile) {
        try {
            return restore(profile);
        } catch (Exception e) {
            throw new FileStoreException("[" + profile + "] 프로필 이미지 저장 실패 ");
        }
    }

}
