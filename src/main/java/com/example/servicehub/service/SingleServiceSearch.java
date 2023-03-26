package com.example.servicehub.service;

import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;

import java.util.Optional;

public interface SingleServiceSearch {
    SingleServiceWithCommentsDto searchWithComments(Long serviceId, Optional<Long> optionalClientId);

    Services search(Long serviceId);
}
