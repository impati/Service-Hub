package com.example.servicehub.service.services;

import com.example.servicehub.domain.services.ServiceCategory;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServicesRegisterForm;
import com.example.servicehub.repository.category.CategoryRepository;
import com.example.servicehub.repository.services.ServiceCategoryRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import com.example.servicehub.support.file.LogoManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ServicesRegister {

    private final ServicesRepository servicesRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final LogoManager logoManager;

    public void registerServices(ServicesRegisterForm servicesRegisterForm) {
        String logoStoreName = returnLogoStoreName(servicesRegisterForm.getLogoUrl(), servicesRegisterForm.getLogoFile());
        Services services = Services.of(
                servicesRegisterForm.getServiceName(),
                logoStoreName,
                servicesRegisterForm.getServicesUrl(),
                servicesRegisterForm.getTitle(),
                servicesRegisterForm.getContent());
        addCategoriesToServices(servicesRegisterForm.getCategoryNames(), services);
        servicesRepository.save(services);
    }

    public void registerServices(List<String> categories, String serviceName,
                                 String serviceUrl, String serviceTitle,
                                 String serviceContent, String logoStoreName) {
        Services services = Services.builder()
                .serviceUrl(serviceUrl)
                .title(serviceTitle)
                .serviceName(serviceName)
                .content(serviceContent)
                .logoStoreName(logoStoreName)
                .build();

        addCategoriesToServices(categories, services);
        servicesRepository.save(services);
    }

    private String returnLogoStoreName(String logoUrl, MultipartFile logoFile) {
        if (isExistLogoUrl(logoUrl)) return logoManager.download(logoUrl);
        return logoManager.tryToRestore(logoFile);
    }

    private boolean isExistLogoUrl(String logoUrl) {
        if (logoUrl != null) return true;
        return false;
    }

    private void addCategoriesToServices(List<String> categories, Services services) {
        serviceCategoryRepository.saveAll(
                categoryRepository.findByNames(categories)
                        .stream()
                        .map(category -> ServiceCategory.of(services, category))
                        .collect(toList()));
    }

}