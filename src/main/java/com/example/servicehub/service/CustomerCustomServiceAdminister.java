package com.example.servicehub.service;

import com.example.servicehub.dto.CustomServiceForm;

public interface CustomerCustomServiceAdminister {
    void addCustomService(Long clientId, CustomServiceForm customServiceForm);

    void deleteCustomService(Long clientId, Long customServiceId);

    String countClickAndReturnUrl(Long clientId, Long customServiceId);
}
