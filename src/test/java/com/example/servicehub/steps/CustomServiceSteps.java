package com.example.servicehub.steps;

import com.example.servicehub.repository.CustomerServiceRepository;

public class CustomServiceSteps {

    private final CustomerServiceRepository customerServiceRepository;

    public CustomServiceSteps(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;

    }

}

