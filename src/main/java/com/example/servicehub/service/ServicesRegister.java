package com.example.servicehub.service;

import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServicesRegisterForm;
import com.example.servicehub.repository.CategoryRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.fileUtils.LogoManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ServicesRegister {
    private final ServicesRepository servicesRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final LogoManager logoManager;

    public void registerServices(ServicesRegisterForm servicesRegisterForm){
        String logoStoreName = logoManager.tryToRestore(servicesRegisterForm.getLogoFile());
        Services services = Services.of(logoStoreName, servicesRegisterForm.getServicesUrl(), servicesRegisterForm.getContent());
        addCategoriesToServices(servicesRegisterForm.getCategoryNames(),services);
        servicesRepository.save(services);
    }

    private void  addCategoriesToServices(List<String> categories,Services services){
        categoryRepository.findByNames(categories)
                .stream()
                .forEach(category -> serviceCategoryRepository.save(ServiceCategory.of(services,category)));
    }

}
