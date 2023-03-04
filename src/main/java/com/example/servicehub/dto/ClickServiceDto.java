package com.example.servicehub.dto;

import com.example.servicehub.domain.CustomService;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private boolean isCustom = false;

    @Builder
    public ClickServiceDto(Long click, Long serviceId ,String serviceName, String logoStoreName, String serviceUrl, String title) {
        this.click = click;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
        this.serviceUrl = serviceUrl;
        this.title = title;
    }

    public static ClickServiceDto from(CustomService customService){
        ClickServiceDto clickServiceDto = ClickServiceDto.builder()
                .serviceName(customService.getServiceName())
                .serviceUrl(customService.getServiceUrl())
                .serviceId(customService.getId())
                .title(customService.getTitle())
                .click(customService.getClickCount())
                .logoStoreName(customService.getLogoStoreName())
                .build();
        clickServiceDto.setCategories(List.of("CUSTOM"));
        clickServiceDto.isCustom = true;
        return clickServiceDto;
    }

    public String getCategories(){
        return String.join(" ",categories);
    }
}
