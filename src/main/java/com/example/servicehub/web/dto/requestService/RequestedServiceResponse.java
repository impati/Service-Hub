package com.example.servicehub.web.dto.requestService;

import java.time.LocalDate;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

	public static RequestedServiceResponse fromEntity(final RequestServiceArticle entity) {
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
