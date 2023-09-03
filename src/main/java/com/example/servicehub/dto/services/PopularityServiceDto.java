package com.example.servicehub.dto.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularityServiceDto {

	private Long popularity;
	private String serviceName;
	private String logoStoreName;
	private String serviceUrl;
	private String title;
	private Long serviceId;
	private boolean isPossess;

	public PopularityServiceDto(
		final Long popularity,
		final String serviceName,
		final String logoStoreName,
		final String serviceUrl,
		final String title,
		final Long serviceId
	) {
		this.popularity = popularity;
		this.serviceName = serviceName;
		this.logoStoreName = logoStoreName;
		this.serviceUrl = serviceUrl;
		this.title = title;
		this.serviceId = serviceId;
		this.isPossess = false;
	}
}
