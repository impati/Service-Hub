package com.example.servicehub.service.services;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.SingleServiceWithCommentsDto;

import java.util.Optional;

public interface SingleServiceSearch {
    SingleServiceWithCommentsDto searchWithComments(Long serviceId, Optional<Long> optionalCustomerId);

    Services search(Long serviceId);
}
