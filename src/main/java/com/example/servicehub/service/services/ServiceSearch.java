package com.example.servicehub.service.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;

public interface ServiceSearch {

	Page<PopularityServiceDto> search(
		final ServiceSearchConditionForm serviceSearchConditionForm,
		final Optional<Long> optionalCustomerId,
		final Pageable pageable
	);
}
