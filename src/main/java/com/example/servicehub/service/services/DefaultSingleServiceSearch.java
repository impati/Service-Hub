package com.example.servicehub.service.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceCommentsDto;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.customer.CustomerServiceRepository;
import com.example.servicehub.repository.services.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class DefaultSingleServiceSearch implements SingleServiceSearch {

	private final ServicesRepository servicesRepository;
	private final CustomerServiceRepository customerServiceRepository;
	private final ServiceCommentsAdminister serviceCommentsAdminister;

	@Override
	public SingleServiceWithCommentsDto searchWithComments(
		final Long serviceId,
		final Optional<Long> optionalCustomerId
	) {
		final Services services = servicesRepository.findByIdUseFetchJoin(serviceId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));

		final boolean isPossess = customerServiceRepository.existsServiceAndCustomerRelationship(
			services.getId(),
			optionalCustomerId.orElse(-1L)
		);

		final List<ServiceCommentsDto> comments = serviceCommentsAdminister.searchComments(services.getId());

		return SingleServiceWithCommentsDto.of(services, isPossess, comments);
	}

	@Override
	public Services search(final Long serviceId) {
		return servicesRepository.findByIdUseFetchJoin(serviceId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));
	}
}
