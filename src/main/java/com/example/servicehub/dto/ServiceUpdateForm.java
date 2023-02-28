package com.example.servicehub.dto;

import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import com.example.servicehub.util.ProjectUtils;
import com.example.servicehub.web.validator.annotation.FileSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Data
@Builder
public class ServiceUpdateForm {

    @NotNull
    private Long serviceId;

    @FileSize(maxSizeInMB = 3,message = "파일 사이즈는 3MB이하로 업로드 해주세요")
    private MultipartFile logoFile;

    @NotBlank(message = "서비스 이름을 입력해주세요")
    private String serviceName;

    @URL(message = "URL 형식을 지켜주세요")
    @NotBlank(message = "서비스 URL을 입력해주세요")
    private String serviceUrl;

    @NotBlank(message = "간단한 서비스 설명을해주세요")
    private String title;

    @NotBlank(message = "서비스에 대해 설명해주세요")
    private String description;

    @NotEmpty(message = "적어도 하나 이상의 카테고리를 설정해주세요")
    private List<String> categoryNames;

    public static ServiceUpdateForm from(Services services){
        return builder()
                .serviceId(services.getId())
                .serviceName(services.getServiceName())
                .serviceUrl(services.getServiceUrl())
                .title(services.getTitle())
                .description(services.getContent())
                .categoryNames(
                        services.getServiceCategories()
                            .stream()
                            .map(ServiceCategory::getCategory)
                            .map(Category::getName)
                            .collect(toList()))
                .build();

    }
}

