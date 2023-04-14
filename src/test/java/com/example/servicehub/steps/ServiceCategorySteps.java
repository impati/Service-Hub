package com.example.servicehub.steps;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServiceCategoryRepository;

public class ServiceCategorySteps {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final ServicesSteps servicesSteps;
    private final CategorySteps categorySteps;

    public ServiceCategorySteps(ServiceCategoryRepository serviceCategoryRepository, ServicesSteps servicesSteps, CategorySteps categorySteps) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.servicesSteps = servicesSteps;
        this.categorySteps = categorySteps;
    }

    public ServiceCategory create(String categoryName, String serviceName, String url) {
        Services services = servicesSteps.create(serviceName, url);
        Category category = categorySteps.create(categoryName);
        return serviceCategoryRepository.save(ServiceCategory.builder()
                .category(category)
                .services(services)
                .build());
    }


    public ServiceCategory create(String categoryName, Services services) {
        Category category = categorySteps.create(categoryName);
        return serviceCategoryRepository.save(ServiceCategory.builder()
                .category(category)
                .services(services)
                .build());
    }

    public ServiceCategory create(Category category, String serviceName, String url) {
        Services services = servicesSteps.create(serviceName, url);
        return serviceCategoryRepository.save(ServiceCategory.builder()
                .category(category)
                .services(services)
                .build());
    }

    public ServiceCategory create(Category category, Services services) {
        return serviceCategoryRepository.save(ServiceCategory.of(services, category));
    }

}
