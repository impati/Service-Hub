package com.example.servicehub.dto;

import com.example.servicehub.domain.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ServicesRegisterForm {
    private List<String> categoryNames;
    private String servicesUrl;
    private String content;
    private MultipartFile logoFile;
    private String serviceName;
    private String title;

    public static ServicesRegisterForm of(List<String> categoryName,String serviceName,String servicesUrl,String title,String content,MultipartFile multipartFile){
        return new ServicesRegisterForm(categoryName,serviceName,servicesUrl,title,content,multipartFile);
    }

    private ServicesRegisterForm(List<String> categoryNames,String serviceName, String servicesUrl, String title,String content, MultipartFile logoFile) {
        this.categoryNames = categoryNames;
        this.servicesUrl = servicesUrl;
        this.content = content;
        this.logoFile = logoFile;
        this.serviceName = serviceName;
        this.title = title;
    }
}
