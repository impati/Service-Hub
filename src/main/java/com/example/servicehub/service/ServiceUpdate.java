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
        services.update(
                serviceUpdateForm.getServiceName(), convertStoreName(services,serviceUpdateForm),
                serviceUpdateForm.getServiceUrl(), serviceUpdateForm.getTitle(),
                serviceUpdateForm.getDescription());

    }

    private String convertStoreName(Services services, ServiceUpdateForm serviceUpdateForm) {
        String newFileName = logoManager.tryToRestore(serviceUpdateForm.getLogoFile());
        String storeName = services.getLogoStoreName();
        if (isExistFile(newFileName)) storeName = newFileName;
        return storeName;
    }

    private boolean isExistFile(String newFileName) {
        if (!newFileName.equals(AbstractFileManager.DEFAULT)) return true;
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