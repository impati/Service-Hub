package com.example.servicehub.service.services;

import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceSearchImpl implements ServiceSearch {

    private final ServicesRepository servicesRepository;
    private final CustomerServiceRepository customerServiceRepository;

    @Override
    public Page<PopularityServiceDto> search(ServiceSearchConditionForm condition,
                                             Optional<Long> optionalCustomer,
                                             Pageable pageRequest) {
        Page<PopularityServiceDto> searchedService = servicesRepository.search(condition.getCategories(), condition.getServiceName(), pageRequest);
        optionalCustomer.ifPresent(customer -> setCustomerPossessServices(customer, searchedService.getContent()));
        return searchedService;
    }

    private void setCustomerPossessServices(Long customerId, List<PopularityServiceDto> services) {
        List<Long> customerServices = customerServiceRepository.findServiceIdOwnedByCustomer(
                services
                        .stream()
                        .map(PopularityServiceDto::getServiceId)
                        .collect(toList()), customerId);

        for (var service : services) {
            if (customerServices.contains(service.getServiceId()))
                service.setPossess(true);
        }
    }

}
