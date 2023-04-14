package com.example.servicehub.service.customer;

import com.example.servicehub.dto.custom.CustomServiceForm;

public interface CustomerCustomServiceAdminister {
    void addCustomService(Long customerId, CustomServiceForm customServiceForm);

    void deleteCustomService(Long customerId, Long customServiceId);

    String countClickAndReturnUrl(Long customerId, Long customServiceId);
}
