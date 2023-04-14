package com.example.servicehub.service.customer;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.dto.custom.CustomServiceForm;
import com.example.servicehub.repository.customer.CustomerCustomServiceRepository;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;
import com.example.servicehub.support.file.LogoManager;
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
    public void addCustomService(Long customerId, CustomServiceForm request) {

        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(request.getServiceUrl());

        String logoStoreName = logoManager.download(serviceMetaData.getImage());

        customerCustomServiceRepository.save(createFrom(serviceMetaData, logoStoreName, request, customerId));
    }

    private CustomService createFrom(ServiceMetaData serviceMetaData, String logoStoreName, CustomServiceForm request, Long customerId) {
        return CustomService.builder()
                .serviceName(request.getServiceName())
                .serviceUrl(request.getServiceUrl())
                .title(serviceMetaData.getTitle())
                .logoStoreName(logoStoreName)
                .customerId(customerId)
                .build();
    }

    @Override
    public void deleteCustomService(Long customerId, Long customServiceId) {
        Optional<CustomService> optionalCustomService = customerCustomServiceRepository
                .findCustomServiceByCustomerIdAndServiceId(customerId, customServiceId);

        optionalCustomService.ifPresent(customerCustomServiceRepository::delete);
    }

    @Override
    public String countClickAndReturnUrl(Long customerId, Long customServiceId) {
        CustomService customService = customerCustomServiceRepository
                .findCustomServiceByCustomerIdAndServiceId(customerId, customServiceId)
                .orElseThrow(IllegalStateException::new);

        customService.click();
        return customService.getServiceUrl();
    }

}

