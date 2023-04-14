package com.example.servicehub.steps;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServicesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicesSteps {

    public static final String DEFAULT_SERVICE_NAME = "Test Service";
    public static final String DEFAULT_LOGO_STORE_NAME = "default.png";
    public static final String DEFAULT_CONTENT = "Test Content";
    public static final String DEFAULT_TITLE = "Test title";
    public static final String DEFAULT_SERVICE_URL = "https://test.com";

    private final ServicesRepository servicesRepository;

    public ServicesSteps(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public Services create() {
        Services services = Services.builder()
                .serviceName(DEFAULT_SERVICE_NAME)
                .logoStoreName(DEFAULT_LOGO_STORE_NAME)
                .content(DEFAULT_CONTENT)
                .title(DEFAULT_TITLE)
                .serviceUrl(DEFAULT_SERVICE_URL)
                .build();

        servicesRepository.save(services);
        return services;
    }

    public Services create(String serviceName, String serviceUrl) {
        Services services = Services.builder()
                .serviceName(serviceName)
                .logoStoreName(UUID.randomUUID().toString())
                .content(DEFAULT_CONTENT)
                .title(DEFAULT_TITLE)
                .serviceUrl(serviceUrl)
                .build();

        servicesRepository.save(services);
        return services;
    }

    public List<Services> creates(int size) {
        List<Services> container = new ArrayList<>();
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
