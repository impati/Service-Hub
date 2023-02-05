package com.example.servicehub.service;

import com.example.servicehub.domain.Client;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface ServiceSearch {
    Page<PopularityServiceDto> search(ServiceSearchConditionForm serviceSearchConditionForm, Optional<Long> optionalClientId, Pageable pageable);
    SingleServiceWithCommentsDto searchSingleService(Long serviceId , Optional<Long> optionalClientId);
}
