package com.example.servicehub.service;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;

import java.util.List;

public interface CustomerServiceSearch {
    List<ClickServiceDto> servicesOfClient(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm);

    List<CustomService> customServicesOfClient(Long clientId, String serviceName);
}
