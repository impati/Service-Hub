package com.example.servicehub.service.services;

import java.util.Optional;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;

public interface SingleServiceSearch {

	SingleServiceWithCommentsDto searchWithComments(final Long serviceId, final Optional<Long> optionalCustomerId);

	Services search(final Long serviceId);
}
