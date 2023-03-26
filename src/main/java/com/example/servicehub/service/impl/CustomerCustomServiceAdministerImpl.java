package com.example.servicehub.service.impl;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.repository.CustomerCustomServiceRepository;
import com.example.servicehub.service.CustomerCustomServiceAdminister;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerCustomServiceAdministerImpl implements CustomerCustomServiceAdminister {

    private final MetaDataCrawler metaDataCrawler;
    private final LogoManager logoManager;
    private final CustomerCustomServiceRepository customerCustomServiceRepository;

    @Override
    public void addCustomService(Long clientId, CustomServiceForm request) {

        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(request.getServiceUrl());

        String logoStoreName = logoManager.download(serviceMetaData.getImage());

        customerCustomServiceRepository.save(createFrom(serviceMetaData, logoStoreName, request, clientId));
    }

    private CustomService createFrom(ServiceMetaData serviceMetaData, String logoStoreName, CustomServiceForm request, Long clientId) {
        return CustomService.builder()
                .serviceName(request.getServiceName())
                .serviceUrl(request.getServiceUrl())
                .title(serviceMetaData.getTitle())
                .logoStoreName(logoStoreName)
                .clientId(clientId)
                .build();
    }

    @Override
    public void deleteCustomService(Long clientId, Long customServiceId) {
        Optional<CustomService> optionalCustomService = customerCustomServiceRepository
                .findCustomServiceByClientIdAndServiceId(clientId, customServiceId);

        optionalCustomService.ifPresent(customerCustomServiceRepository::delete);
    }

    @Override
    public String countClickAndReturnUrl(Long clientId, Long customServiceId) {
        CustomService customService = customerCustomServiceRepository
                .findCustomServiceByClientIdAndServiceId(clientId, customServiceId)
                .orElseThrow(IllegalStateException::new);

        customService.click();
        return customService.getServiceUrl();
    }

}

