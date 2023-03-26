package com.example.servicehub.service.impl;

import com.example.servicehub.domain.CustomerService;
import com.example.servicehub.repository.CustomerServiceRepository;
import com.example.servicehub.service.ServiceClickCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultServiceClickCounter implements ServiceClickCounter {
    private final CustomerServiceRepository customerServiceRepository;

    @Override
    public String countClickAndReturnUrl(Long clientId, Long serviceId) {
        CustomerService customerService = customerServiceRepository.findClientServiceBy(clientId, serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 메서드 호출입니다."));
        customerService.click();
        return customerService.getServices().getServiceUrl();
    }
}
