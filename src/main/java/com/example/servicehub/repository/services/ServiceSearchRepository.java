package com.example.servicehub.repository.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.PopularityServiceDto;

public interface ServiceSearchRepository {

    Page<PopularityServiceDto> search(
        final List<String> categories,
        final String serviceName,
        final Pageable pageable
    );

    List<ClickServiceDto> searchByCustomer(
        final Long customerId,
        final List<String> categories,
        final String serviceName
    );
}
