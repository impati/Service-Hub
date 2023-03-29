package com.example.servicehub.service;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;

import java.util.List;

public interface CustomerServiceSearch {
    List<ClickServiceDto> servicesOfCustomer(Long customerId, ServiceSearchConditionForm serviceSearchConditionForm);

    List<CustomService> customServicesOfCustomer(Long customerId, String serviceName);
}
