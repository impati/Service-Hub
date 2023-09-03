package com.example.servicehub.dto.services;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceSearchConditionForm {

	private List<String> categories;
	private String serviceName;

	public static ServiceSearchConditionForm of(final List<String> categories, final String serviceName) {
		return new ServiceSearchConditionForm(categories, serviceName);
	}

	private ServiceSearchConditionForm(final List<String> categories, final String serviceName) {
		this.categories = categories;
		this.serviceName = serviceName;
	}
}
