package com.example.servicehub.config;

import com.example.servicehub.repository.*;
import com.example.servicehub.steps.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class StepsConfig {

    private final ServicesRepository servicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final ServiceCommentRepository serviceCommentRepository;

    public StepsConfig(ServicesRepository servicesRepository, ServiceCategoryRepository serviceCategoryRepository, CategoryRepository categoryRepository, CustomerServiceRepository customerServiceRepository, ServiceCommentRepository serviceCommentRepository) {
        this.servicesRepository = servicesRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.customerServiceRepository = customerServiceRepository;
        this.serviceCommentRepository = serviceCommentRepository;
    }

    @Bean
    public ServiceCategorySteps serviceCategorySteps() {
        return new ServiceCategorySteps(serviceCategoryRepository, servicesSteps(), categorySteps());
    }

    @Bean
    public ServicesSteps servicesSteps() {
        return new ServicesSteps(servicesRepository);
    }

    @Bean
    public CategorySteps categorySteps() {
        return new CategorySteps(categoryRepository);
    }

    @Bean
    public CustomerServiceSteps customerServiceSteps() {
        return new CustomerServiceSteps(customerServiceRepository);
    }

    @Bean
    public ServiceCommentsSteps serviceCommentsSteps() {
        return new ServiceCommentsSteps(serviceCommentRepository);
    }
}
