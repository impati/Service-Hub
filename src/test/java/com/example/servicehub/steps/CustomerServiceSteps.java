package com.example.servicehub.steps;

import com.example.servicehub.domain.CustomerService;
import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.CustomerServiceRepository;

public class CustomerServiceSteps {

    private final CustomerServiceRepository customerServiceRepository;

    public CustomerServiceSteps(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

    public CustomerService create(Long clientId, Services services) {
        return customerServiceRepository.save(CustomerService
                .builder()
                .services(services)
                .clientId(clientId)
                .build());
    }

    public CustomerService createWithClick(Long clientId, Services services, int click) {
        CustomerService customerService = CustomerService.of(clientId, services);
        for (int i = 0; i < click; i++)
            customerService.click();
        return customerServiceRepository.save(customerService);
    }

}
