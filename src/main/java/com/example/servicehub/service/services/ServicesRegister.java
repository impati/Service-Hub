package com.example.servicehub.service.services;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServicesRegisterForm;
import com.example.servicehub.repository.category.CategoryRepository;
import com.example.servicehub.repository.services.ServiceCategoryRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.support.file.LogoManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ServicesRegister {

	private final ServicesRepository servicesRepository;
	private final CategoryRepository categoryRepository;
	private final ServiceCategoryRepository serviceCategoryRepository;
	private final LogoManager logoManager;

	public void registerServices(final ServicesRegisterForm servicesRegisterForm) {
		final String logoStoreName = returnLogoStoreName(
			servicesRegisterForm.getLogoUrl(),
			servicesRegisterForm.getLogoFile()
		);
        
		final Services services = Services.of(
			servicesRegisterForm.getServiceName(),
			logoStoreName,
			servicesRegisterForm.getServicesUrl(),
			servicesRegisterForm.getTitle(),
			servicesRegisterForm.getContent());

		addCategoriesToServices(servicesRegisterForm.getCategoryNames(), services);
		servicesRepository.save(services);
	}

	public void registerServices(
		final List<String> categories,
		final String serviceName,
		final String serviceUrl,
		final String serviceTitle,
		final String serviceContent,
		final String logoStoreName
	) {
		final Services services = Services.builder()
			.serviceUrl(serviceUrl)
			.title(serviceTitle)
			.serviceName(serviceName)
			.content(serviceContent)
			.logoStoreName(logoStoreName)
			.build();

		addCategoriesToServices(categories, services);
		servicesRepository.save(services);
	}

	private String returnLogoStoreName(final String logoUrl, final MultipartFile logoFile) {
		if (isExistLogoUrl(logoUrl)) {
			return logoManager.download(logoUrl);
		}

		return logoManager.tryToRestore(logoFile);
	}

	private boolean isExistLogoUrl(final String logoUrl) {
		return logoUrl != null;
	}

	private void addCategoriesToServices(final List<String> categories, final Services services) {
		serviceCategoryRepository.saveAll(categoryRepository.findByNames(categories).stream()
			.map(category -> ServiceCategory.of(services, category))
			.collect(toList()));
	}
}
