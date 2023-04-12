package com.example.servicehub.web.dto;

import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class RequestedServiceResponse {
    private Long id;
    private String nickname;
    private String articleTitle;
    private String articleDescription;
    private String serviceUrl;
    private String logoStoreName;
    private String serviceTitle;
    private String serviceName;
    private String serviceContent;
    private RequestStatus requestStatus;
    private LocalDate createdAt;
    private LocalDate updatedAt;


    public static RequestedServiceResponse fromEntity(RequestServiceArticle entity) {
        return RequestedServiceResponse.builder()
                .id(entity.getId())
                .nickname(entity.getNickname())
                .articleTitle(entity.getArticleTitle())
                .articleDescription(entity.getArticleDescription())
                .serviceUrl(entity.getServiceUrl())
                .serviceTitle(entity.getServiceTitle())
                .serviceName(entity.getServiceName())
                .serviceContent(entity.getServiceContent())
                .requestStatus(entity.getRequestStatus())
                .logoStoreName(entity.getLogoStoreName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
