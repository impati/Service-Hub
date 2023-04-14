package com.example.servicehub.service.services;

import com.example.servicehub.dto.services.PopularityServiceDto;
import com.example.servicehub.dto.services.ServiceSearchConditionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ServiceSearch {
    Page<PopularityServiceDto> search(ServiceSearchConditionForm serviceSearchConditionForm, Optional<Long> optionalCustomerId, Pageable pageable);
}
