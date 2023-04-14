package com.example.servicehub.steps;

import com.example.servicehub.domain.customer.CustomerService;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.customer.CustomerServiceRepository;

public class CustomerServiceSteps {

    private final CustomerServiceRepository customerServiceRepository;

    public CustomerServiceSteps(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

    public CustomerService create(Long customerId, Services services) {
        return customerServiceRepository.save(CustomerService
                .builder()
                .services(services)
                .customerId(customerId)
                .build());
    }

    public CustomerService createWithClick(Long customerId, Services services, int click) {
        CustomerService customerService = CustomerService.of(customerId, services);
        for (int i = 0; i < click; i++)
            customerService.click();
        return customerServiceRepository.save(customerService);
    }

}
