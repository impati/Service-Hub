package com.example.servicehub.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.servicehub.repository.category.CategoryRepository;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServiceCategoryRepository;
import com.example.servicehub.repository.services.ServiceCommentRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.steps.CategorySteps;
import com.example.servicehub.steps.CustomerServiceSteps;
import com.example.servicehub.steps.ServiceCategorySteps;
import com.example.servicehub.steps.ServiceCommentsSteps;
import com.example.servicehub.steps.ServicesSteps;

@TestConfiguration
public class StepsConfig {

    private final ServicesRepository servicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final ServiceCommentRepository serviceCommentRepository;

    public StepsConfig(
        final ServicesRepository servicesRepository,
        final ServiceCategoryRepository serviceCategoryRepository,
        final CategoryRepository categoryRepository,
        final CustomerServiceRepository customerServiceRepository,
        final ServiceCommentRepository serviceCommentRepository
    ) {
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
