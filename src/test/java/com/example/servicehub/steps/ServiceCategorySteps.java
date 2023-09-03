package com.example.servicehub.steps;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServiceCategoryRepository;

public class ServiceCategorySteps {

	private final ServiceCategoryRepository serviceCategoryRepository;
	private final ServicesSteps servicesSteps;
	private final CategorySteps categorySteps;

	public ServiceCategorySteps(
		final ServiceCategoryRepository serviceCategoryRepository,
		final ServicesSteps servicesSteps,
		final CategorySteps categorySteps
	) {
		this.serviceCategoryRepository = serviceCategoryRepository;
		this.servicesSteps = servicesSteps;
		this.categorySteps = categorySteps;
	}

	public ServiceCategory create(
		final String categoryName,
		final String serviceName,
		final String url
	) {
		final Services services = servicesSteps.create(serviceName, url);
		final Category category = categorySteps.create(categoryName);
		return serviceCategoryRepository.save(ServiceCategory.builder()
			.category(category)
			.services(services)
			.build());
	}

	public ServiceCategory create(final String categoryName, final Services services) {
		final Category category = categorySteps.create(categoryName);
		return serviceCategoryRepository.save(ServiceCategory.builder()
			.category(category)
			.services(services)
			.build());
	}

	public ServiceCategory create(
		final Category category,
		final String serviceName,
		final String url
	) {
		final Services services = servicesSteps.create(serviceName, url);
		return serviceCategoryRepository.save(ServiceCategory.builder()
			.category(category)
			.services(services)
			.build());
	}

	public ServiceCategory create(final Category category, final Services services) {
		return serviceCategoryRepository.save(ServiceCategory.of(services, category));
	}
}
