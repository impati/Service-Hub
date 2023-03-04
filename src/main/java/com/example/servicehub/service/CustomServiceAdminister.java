package com.example.servicehub.service;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.CustomServiceForm;

import java.util.List;

public interface CustomServiceAdminister {
    void addCustomService(Long clientId, CustomServiceForm customServiceForm);

    void deleteCustomService(Long clientId,Long customServiceId);

    List<CustomService> customServicesOfClient(Long clientId,String serviceName);

    String countClickAndReturnUrl(Long clientId,Long customServiceId);
}
