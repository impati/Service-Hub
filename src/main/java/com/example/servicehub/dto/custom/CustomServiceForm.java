package com.example.servicehub.dto.custom;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomServiceForm {

	@NotBlank
	private String serviceName;

	@NotBlank
	@URL
	private String serviceUrl;
}
