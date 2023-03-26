package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServiceCommentsDto;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.CustomerServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.SingleServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class DefaultSingleServiceSearch implements SingleServiceSearch {

    private final ServicesRepository servicesRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final ServiceCommentsAdminister serviceCommentsAdminister;

    @Override
    public SingleServiceWithCommentsDto searchWithComments(Long serviceId, Optional<Long> optionalClientId) {

        Services services = servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));

        boolean isPossess = customerServiceRepository.existsServiceAndClientRelationship(services.getId(), optionalClientId.orElse(-1L));

        List<ServiceCommentsDto> comments = serviceCommentsAdminister.searchComments(services.getId());

        return SingleServiceWithCommentsDto.of(services, isPossess, comments);
    }

    @Override
    public Services search(Long serviceId) {
        return servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));
    }
}
