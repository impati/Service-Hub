package com.example.servicehub.service;

public interface CustomerServiceAdminister {

    void addCustomerService(Long customerId, Long serviceId);

    void deleteCustomerService(Long customerId, Long serviceId);
}
