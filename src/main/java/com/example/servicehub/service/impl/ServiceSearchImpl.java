package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceCommentsDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.ServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceSearchImpl implements ServiceSearch {

    private final ServicesRepository servicesRepository;
    private final ClientServiceRepository clientServiceRepository;
    private final ServiceCommentsAdminister serviceCommentsAdminister;

    @Override
    public Page<PopularityServiceDto> search(ServiceSearchConditionForm serviceSearchConditionForm,
                                             Optional<Long> optionalClient,
                                             Pageable pageRequest
                                             ) {

        Page<PopularityServiceDto> searchedService = servicesRepository.search(serviceSearchConditionForm.getCategories(), serviceSearchConditionForm.getServiceName(),pageRequest);

        optionalClient.ifPresent(client -> setClientPossessServices(client, searchedService.getContent()));

        return searchedService;
    }

    private void setClientPossessServices(Long clientId , List<PopularityServiceDto> services){
        List<Long> clientServices  = clientServiceRepository.findServiceIdOwnedByClient(
                services
                        .stream()
                        .map(PopularityServiceDto::getServiceId)
                        .collect(toList()), clientId);

        for(var service : services){
            if(clientServices.contains(service.getServiceId()))
                service.setPossess(true);
        }
    }



    @Override
    public SingleServiceWithCommentsDto searchSingleService(Long serviceId, Optional<Long> optionalClientId) {

        Services services = servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));

        boolean isPossess = clientServiceRepository.existsServiceAndClientRelationship(services.getId(), optionalClientId.orElse(-1L));

        List<ServiceCommentsDto> comments = serviceCommentsAdminister.searchComments(services.getId());

        return SingleServiceWithCommentsDto.of(services,isPossess,comments);
    }

    @Override
    public Services search(Long serviceId) {
        return servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));
    }

}
