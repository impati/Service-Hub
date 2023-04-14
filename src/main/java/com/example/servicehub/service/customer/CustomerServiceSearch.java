package com.example.servicehub.service.customer;

import com.example.servicehub.domain.custom.CustomService;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;

import java.util.List;

public interface CustomerServiceSearch {
    List<ClickServiceDto> servicesOfCustomer(Long customerId, ServiceSearchConditionForm serviceSearchConditionForm);

    List<CustomService> customServicesOfCustomer(Long customerId, String serviceName);
}
