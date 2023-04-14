package com.example.servicehub.service.services;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceUpdate {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ServicesRepository servicesRepository;
    private final LogoManager logoManager;

    public void update(ServiceUpdateForm serviceUpdateForm) {

        Services services = servicesRepository.findById(serviceUpdateForm.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));

        updateCategories(services, serviceUpdateForm.getCategoryNames());

        String storeName = getStoreName(serviceUpdateForm);

        if (isDefaultFile(storeName)) storeName = services.getLogoStoreName();

        services.update(
                serviceUpdateForm.getServiceName(), storeName,
                serviceUpdateForm.getServiceUrl(), serviceUpdateForm.getTitle(),
                serviceUpdateForm.getDescription());

    }

    private String getStoreName(ServiceUpdateForm serviceUpdateForm) {
        return logoManager.tryToRestore(serviceUpdateForm.getLogoFile());
    }

    private boolean isDefaultFile(String fileName) {
        if (fileName.equals(AbstractFileManager.DEFAULT)) return true;
        return false;
    }

    private void updateCategories(Services services, List<String> categoryNames) {

        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findByServices(services);

        List<ServiceCategory> newServiceCategories =
                categoryRepository.findByNames(categoryNames)
                        .stream()
                        .map(categories -> ServiceCategory.of(services, categories))
                        .collect(toList());

        serviceCategoryRepository.deleteAll(
                serviceCategories
                        .stream()
                        .filter(serviceCategory -> !newServiceCategories.contains(serviceCategory))
                        .collect(toList()));

        serviceCategoryRepository.saveAll(
                newServiceCategories
                        .stream()
                        .filter(serviceCategory -> !serviceCategories.contains(serviceCategory))
                        .collect(toList()));
    }

}