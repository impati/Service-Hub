package com.example.servicehub.dto;

import com.example.servicehub.domain.Services;
import com.example.servicehub.web.validator.annotation.FileSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ServicesRegisterForm {

    @NotNull(message = "적어도 한개 이상의 카테고리를 선택해주세요")
    @NotEmpty(message = "적어도 한개 이상의 카테고리를 선택해주세요")
    private List<String> categoryNames;

    @URL(message = "URL 형식을 지켜주세요")
    @NotBlank(message = "빈 값일 수 없습니다.")
    private String servicesUrl;

    @NotBlank(message = "빈 값일 수 없습니다.")
    private String content;

    @FileSize(maxSizeInMB = 3,message = "로고 이미지 크기는 3MB이하로 업로드 해주세요.")
    private MultipartFile logoFile;

    @URL(message = "URL 형식을 지켜주세요")
    private String logoUrl;

    @NotBlank(message = "빈 값일 수 없습니다.")
    private String serviceName;

    @NotBlank(message = "빈 값일 수 없습니다.")
    private String title;

    public static ServicesRegisterForm of(List<String> categoryName,String serviceName,String servicesUrl,String title,String content,MultipartFile multipartFile){
        return new ServicesRegisterForm(categoryName,serviceName,servicesUrl,title,content,multipartFile);
    }

    public static ServicesRegisterForm of(List<String> categoryName,String serviceName,String servicesUrl,String title,String content,String logoUrl){
        return new ServicesRegisterForm(categoryName,serviceName,servicesUrl,title,content,logoUrl);
    }

    public static ServicesRegisterForm of(String siteName, String url, String title, String description,String image) {
        return new ServicesRegisterForm(new ArrayList<>(),siteName,url,title,description,image);
    }

    private ServicesRegisterForm(List<String> categoryNames, String serviceName, String servicesUrl, String title, String content, String logoUrl) {
        this.categoryNames = categoryNames;
        this.servicesUrl = servicesUrl;
        this.content = content;
        this.logoUrl = logoUrl;
        this.serviceName = serviceName;
        this.title = title;
    }

    private ServicesRegisterForm(List<String> categoryNames, String serviceName, String servicesUrl, String title, String content, MultipartFile logoFile) {
        this.categoryNames = categoryNames;
        this.servicesUrl = servicesUrl;
        this.content = content;
        this.logoFile = logoFile;
        this.serviceName = serviceName;
        this.title = title;
    }


}
