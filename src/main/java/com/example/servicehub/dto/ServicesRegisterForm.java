package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ServicesRegisterForm {
    private List<String> categoryNames;
    private String servicesUrl;
    private String content;
    private MultipartFile logoFile;

    public static ServicesRegisterForm of(List<String> categoryName,String servicesUrl,String content,MultipartFile multipartFile){
        return new ServicesRegisterForm(categoryName,servicesUrl,content,multipartFile);
    }

    public ServicesRegisterForm(List<String> categoryNames, String servicesUrl, String content, MultipartFile logoFile) {
        this.categoryNames = categoryNames;
        this.servicesUrl = servicesUrl;
        this.content = content;
        this.logoFile = logoFile;
    }
}
