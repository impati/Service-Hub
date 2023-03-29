package com.example.servicehub.service.impl;

import com.example.servicehub.domain.CustomService;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.CustomerCustomServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.CustomerServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultCustomerServiceSearch implements CustomerServiceSearch {

    private final ServicesRepository servicesRepository;
    private final CustomerCustomServiceRepository customerCustomServiceRepository;

    @Override
    @Transactional
    public List<ClickServiceDto> servicesOfCustomer(Long customerId, ServiceSearchConditionForm condition) {
        return servicesRepository.searchByCustomer(customerId, condition.getCategories(), condition.getServiceName())
                .stream()
                .distinct()
                .sorted(Comparator.comparing(ClickServiceDto::getClick).reversed())
                .collect(toList());
    }

    @Override
    public List<CustomService> customServicesOfCustomer(Long customerId, String serviceName) {
        if (StringUtils.hasText(serviceName))
            return customerCustomServiceRepository.findCustomServiceByCustomerIdAndServiceName(customerId, serviceName);
        return customerCustomServiceRepository.findCustomServiceByCustomerId(customerId);
    }

}

