package com.example.servicehub.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServicesRepository;

public class ServicesSteps {

	private static final String DEFAULT_SERVICE_NAME = "Test Service";
	private static final String DEFAULT_LOGO_STORE_NAME = "default.png";
	private static final String DEFAULT_CONTENT = "Test Content";
	private static final String DEFAULT_TITLE = "Test title";
	private static final String DEFAULT_SERVICE_URL = "https://test.com";

	private final ServicesRepository servicesRepository;

	public ServicesSteps(final ServicesRepository servicesRepository) {
		this.servicesRepository = servicesRepository;
	}

	public Services create() {
		final Services services = Services.builder()
			.serviceName(DEFAULT_SERVICE_NAME)
			.logoStoreName(DEFAULT_LOGO_STORE_NAME)
			.content(DEFAULT_CONTENT)
			.title(DEFAULT_TITLE)
			.serviceUrl(DEFAULT_SERVICE_URL)
			.build();

		servicesRepository.save(services);
		return services;
	}

	public Services create(final String serviceName, final String serviceUrl) {
		final Services services = Services.builder()
			.serviceName(serviceName)
			.logoStoreName(UUID.randomUUID().toString())
			.content(DEFAULT_CONTENT)
			.title(DEFAULT_TITLE)
			.serviceUrl(serviceUrl)
			.build();

		servicesRepository.save(services);
		return services;
	}

	public List<Services> creates(final int size) {
		final List<Services> container = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			container.add(Services.builder()
				.serviceName(DEFAULT_SERVICE_NAME + i)
				.logoStoreName(DEFAULT_LOGO_STORE_NAME + i)
				.content(DEFAULT_CONTENT)
				.title(DEFAULT_TITLE)
				.serviceUrl(DEFAULT_SERVICE_URL + i)
				.build());
		}

		servicesRepository.saveAll(container);
		return container;
	}
}
