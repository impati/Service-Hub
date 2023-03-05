package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.CustomServiceRepository;
import com.example.servicehub.service.CustomServiceAdminister;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomServiceAdministerImpl implements CustomServiceAdminister {

    private final MetaDataCrawler metaDataCrawler;
    private final LogoManager logoManager;
    private final CustomServiceRepository customServiceRepository;
    private final ClientRepository clientRepository;

    @Override
    public void addCustomService(Long clientId, CustomServiceForm request) {

        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData(request.getServiceUrl());

        String logoStoreName = logoManager.download(serviceMetaData.getUrl());

        Client client = clientRepository.findById(clientId).orElseThrow(IllegalStateException::new);

        customServiceRepository.save(createFrom(serviceMetaData,logoStoreName,request,client));
    }

    private CustomService createFrom(ServiceMetaData serviceMetaData , String logoStoreName , CustomServiceForm request , Client client){
        return CustomService.builder()
                .serviceName(request.getServiceName())
                .serviceUrl(request.getServiceUrl())
                .title(serviceMetaData.getTitle())
                .logoStoreName(logoStoreName)
                .client(client)
                .build();
    }

    @Override
    public void deleteCustomService(Long clientId, Long customServiceId) {
        Optional<CustomService> optionalCustomService = customServiceRepository
                .findCustomServiceByClientIdAndServiceId(clientId,customServiceId);

        optionalCustomService.ifPresent(customServiceRepository::delete);
    }

    @Override
    public List<CustomService> customServicesOfClient(Long clientId,String serviceName) {
        if(StringUtils.hasText(serviceName)) return customServiceRepository.findCustomServiceByClientIdAndServiceName(clientId,serviceName);
        return customServiceRepository.findCustomServiceByClientId(clientId);
    }

    @Override
    public String countClickAndReturnUrl(Long clientId, Long customServiceId) {
        CustomService customService = customServiceRepository
                .findCustomServiceByClientIdAndServiceId(clientId,customServiceId)
                .orElseThrow(IllegalStateException::new);

        customService.click();
        return customService.getServiceUrl();
    }

}

