package com.example.servicehub.service.impl;

import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.CustomerServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceSearch;
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
                                             Optional<Long> optionalClient,
                                             Pageable pageRequest) {
        Page<PopularityServiceDto> searchedService = servicesRepository.search(condition.getCategories(), condition.getServiceName(), pageRequest);
        optionalClient.ifPresent(client -> setClientPossessServices(client, searchedService.getContent()));
        return searchedService;
    }

    private void setClientPossessServices(Long clientId, List<PopularityServiceDto> services) {
        List<Long> clientServices = customerServiceRepository.findServiceIdOwnedByClient(
                services
                        .stream()
                        .map(PopularityServiceDto::getServiceId)
                        .collect(toList()), clientId);

        for (var service : services) {
            if (clientServices.contains(service.getServiceId()))
                service.setPossess(true);
        }
    }

}
