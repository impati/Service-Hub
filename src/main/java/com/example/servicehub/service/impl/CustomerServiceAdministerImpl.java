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
    public void addClientService(Long clientId, Long serviceId) {
        Services services = findService(serviceId);
        if (alreadyExistForClient(clientId, services)) return;
        customerServiceRepository.save(CustomerService.of(clientId, services));
    }

    private Services findService(Long serviceId) {
        return servicesRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));
    }

    private boolean alreadyExistForClient(Long clientId, Services services) {
        return customerServiceRepository.alreadyExistsServiceForClient(clientId, services);
    }

    @Override
    public void deleteClientService(Long clientId, Long serviceId) {
        Optional<CustomerService> optionalClientService = customerServiceRepository.findClientServiceBy(clientId, serviceId);
        optionalClientService.ifPresent(customerServiceRepository::delete);
    }
}
