package com.example.servicehub.dto;

import com.example.servicehub.domain.Services;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class SingleServiceWithCommentsDto {

    private String logoStoreName;
    private String serviceName;
    private String serviceUrl;
    private String title;
    private String content;
    private List<String> categories;
    private List<ServiceCommentsDto> comments;
    private boolean isPossess;

    public static SingleServiceWithCommentsDto of(Services services , boolean isPossess , List<ServiceCommentsDto> comments){
        SingleServiceWithCommentsDto singleServiceWithCommentsDto = new SingleServiceWithCommentsDto();
        singleServiceWithCommentsDto.logoStoreName = services.getLogoStoreName();
        singleServiceWithCommentsDto.serviceName = services.getServiceName();
        singleServiceWithCommentsDto.serviceUrl = services.getServiceUrl();
        singleServiceWithCommentsDto.title = services.getTitle();
        singleServiceWithCommentsDto.content = services.getContent();
        singleServiceWithCommentsDto.categories =
                services.getServiceCategories()
                        .stream()
                        .map(serviceCategory -> serviceCategory.getCategory().getName())
                        .collect(Collectors.toList());
        singleServiceWithCommentsDto.isPossess = isPossess;
        singleServiceWithCommentsDto.comments = comments;
        return singleServiceWithCommentsDto;
    }
}
