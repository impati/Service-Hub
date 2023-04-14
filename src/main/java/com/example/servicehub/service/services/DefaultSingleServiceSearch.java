package com.example.servicehub.service.services;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceCommentsDto;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;
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
    public SingleServiceWithCommentsDto searchWithComments(Long serviceId, Optional<Long> optionalcustomerId) {

        Services services = servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));

        boolean isPossess = customerServiceRepository.existsServiceAndCustomerRelationship(services.getId(), optionalcustomerId.orElse(-1L));

        List<ServiceCommentsDto> comments = serviceCommentsAdminister.searchComments(services.getId());

        return SingleServiceWithCommentsDto.of(services, isPossess, comments);
    }

    @Override
    public Services search(Long serviceId) {
        return servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));
    }
}
