package com.example.servicehub.dto.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.domain.services.Services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
	public ClickServiceDto(
		final Long click,
		final Long serviceId,
		final String serviceName,
		final String logoStoreName,
		final String serviceUrl,
		final String title
	) {
		this.click = click;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.logoStoreName = logoStoreName;
		this.serviceUrl = serviceUrl;
		this.title = title;
	}

	public ClickServiceDto(final Services services, final Long click) {
		this.click = click;
		this.serviceId = services.getId();
		this.serviceName = services.getServiceName();
		this.logoStoreName = services.getLogoStoreName();
		this.serviceUrl = services.getServiceUrl();
		this.title = services.getTitle();
		this.categories = services
			.getServiceCategories()
			.stream()
			.map(serviceCategory -> serviceCategory.getCategory().getName())
			.collect(Collectors.toList());
	}

	public static ClickServiceDto from(final Services services) {
		final ClickServiceDto clickServiceDto = ClickServiceDto.builder()
			.serviceName(services.getServiceName())
			.serviceUrl(services.getServiceUrl())
			.serviceId(services.getId())
			.title(services.getTitle())
			.click(services.getCustomerServices().stream().mapToLong(CustomerService::getClickCount).sum())
			.logoStoreName(services.getLogoStoreName())
			.build();
		clickServiceDto.categories = services
			.getServiceCategories()
			.stream()
			.map(serviceCategory -> serviceCategory.getCategory().getName())
			.collect(Collectors.toList());
		return clickServiceDto;
	}

	public static ClickServiceDto from(final CustomService customService) {
		final ClickServiceDto clickServiceDto = ClickServiceDto.builder()
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

	public String getCategories() {
		return String.join(" ", categories);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ClickServiceDto))
			return false;
		ClickServiceDto that = (ClickServiceDto)o;
		return Objects.equals(serviceId, that.serviceId);
	}

	@Override
	public int hashCode() {
		return serviceId != null ? serviceId.hashCode() : 0;
	}
}
