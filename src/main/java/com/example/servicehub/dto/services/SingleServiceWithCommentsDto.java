package com.example.servicehub.dto.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.servicehub.domain.services.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleServiceWithCommentsDto {

	private Long serviceId;
	private String logoStoreName;
	private String serviceName;
	private String serviceUrl;
	private String title;
	private String content;
	private List<String> categories = new ArrayList<>();
	private List<ServiceCommentsDto> comments = new ArrayList<>();
	private boolean isPossess;

	public static SingleServiceWithCommentsDto of(
		final Services services,
		final boolean isPossess,
		final List<ServiceCommentsDto> comments
	) {
		final SingleServiceWithCommentsDto singleServiceWithCommentsDto = new SingleServiceWithCommentsDto();
		singleServiceWithCommentsDto.serviceId = services.getId();
		singleServiceWithCommentsDto.logoStoreName = services.getLogoStoreName();
		singleServiceWithCommentsDto.serviceName = services.getServiceName();
		singleServiceWithCommentsDto.serviceUrl = services.getServiceUrl();
		singleServiceWithCommentsDto.title = services.getTitle();
		singleServiceWithCommentsDto.content = services.getContent();
		singleServiceWithCommentsDto.categories = services.getServiceCategories().stream()
			.map(serviceCategory -> serviceCategory.getCategory().getName())
			.collect(Collectors.toList());
		singleServiceWithCommentsDto.isPossess = isPossess;
		singleServiceWithCommentsDto.comments = comments;
		return singleServiceWithCommentsDto;
	}
}
