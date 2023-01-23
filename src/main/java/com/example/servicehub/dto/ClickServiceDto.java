package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ClickServiceDto {
    private Long click;
    private Long serviceId;
    private String serviceName;
    private String logoStoreName;
    private String serviceUrl;
    private String title;
    private List<String> categories = new ArrayList<>();

    public ClickServiceDto(Long click, Long serviceId ,String serviceName, String logoStoreName, String serviceUrl, String title) {
        this.click = click;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
        this.serviceUrl = serviceUrl;
        this.title = title;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCategories(){
        StringBuilder stringBuilder = new StringBuilder();
        for(var category: categories){
            stringBuilder.append(category + " ");
        }
        return stringBuilder.toString();
    }
}
