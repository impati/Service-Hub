package com.example.servicehub.service.impl;

import com.example.servicehub.domain.CustomerService;
import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.CustomerServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.CustomerServiceAdminister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceAdministerImpl implements CustomerServiceAdminister {

    private final CustomerServiceRepository customerServiceRepository;
    private final ServicesRepository servicesRepository;

    @Override
    public void addCustomerService(Long customerId, Long serviceId) {
        Services services = findService(serviceId);
        if (alreadyExistForcustomer(customerId, services)) return;
        customerServiceRepository.save(CustomerService.of(customerId, services));
    }

    private Services findService(Long serviceId) {
        return servicesRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));
    }

    private boolean alreadyExistForcustomer(Long customerId, Services services) {
        return customerServiceRepository.alreadyExistsServiceForCustomer(customerId, services);
    }

    @Override
    public void deleteCustomerService(Long customerId, Long serviceId) {
        Optional<CustomerService> optionalcustomerService = customerServiceRepository.findCustomerServiceBy(customerId, serviceId);
        optionalcustomerService.ifPresent(customerServiceRepository::delete);
    }
}
