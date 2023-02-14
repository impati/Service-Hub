package com.example.servicehub.service;

import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServiceUpdateForm;
import com.example.servicehub.repository.CategoryRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.support.AbstractFileManager;
import com.example.servicehub.support.LogoManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

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

        updateCategories(services,serviceUpdateForm.getCategoryNames());

        String storeName = getStoreName(serviceUpdateForm);

        if(isDefaultFile(storeName)) storeName = services.getLogoStoreName();

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