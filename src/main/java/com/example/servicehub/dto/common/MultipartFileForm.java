package com.example.servicehub.dto.common;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartFileForm {
    protected MultipartFile file;
    protected String storeName;
}
