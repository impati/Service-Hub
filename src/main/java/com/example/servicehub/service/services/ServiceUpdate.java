package com.example.servicehub.service.services;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceUpdateForm;
import com.example.servicehub.repository.category.CategoryRepository;
import com.example.servicehub.repository.services.ServiceCategoryRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.support.file.AbstractFileManager;
import com.example.servicehub.support.file.LogoManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceUpdate {

	private final ServiceCategoryRepository serviceCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final ServicesRepository servicesRepository;
	private final LogoManager logoManager;

	public void update(final ServiceUpdateForm serviceUpdateForm) {
		final Services services = servicesRepository.findById(serviceUpdateForm.getServiceId())
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));

		updateCategories(services, serviceUpdateForm.getCategoryNames());

		String storeName = getStoreName(serviceUpdateForm);
		if (isDefaultFile(storeName)) {
			storeName = services.getLogoStoreName();
		}

		services.update(
			serviceUpdateForm.getServiceName(), storeName,
			serviceUpdateForm.getServiceUrl(), serviceUpdateForm.getTitle(),
			serviceUpdateForm.getDescription());
	}

	private String getStoreName(final ServiceUpdateForm serviceUpdateForm) {
		return logoManager.tryToRestore(serviceUpdateForm.getLogoFile());
	}

	private boolean isDefaultFile(final String fileName) {
		return fileName.equals(AbstractFileManager.DEFAULT);
	}

	private void updateCategories(final Services services, final List<String> categoryNames) {
		final List<ServiceCategory> serviceCategories = serviceCategoryRepository.findByServices(services);

		final List<ServiceCategory> newServiceCategories = categoryRepository.findByNames(categoryNames).stream()
			.map(categories -> ServiceCategory.of(services, categories))
			.collect(toList());

		serviceCategoryRepository.deleteAll(serviceCategories.stream()
			.filter(serviceCategory -> !newServiceCategories.contains(serviceCategory))
			.collect(toList()));

		serviceCategoryRepository.saveAll(newServiceCategories.stream()
			.filter(serviceCategory -> !serviceCategories.contains(serviceCategory))
			.collect(toList()));
	}
}
