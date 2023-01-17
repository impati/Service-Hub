package com.example.servicehub.service;

import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import org.springframework.data.domain.Page;


public interface ServiceSearch {
    Page<PopularityServiceDto> search(ServiceSearchConditionForm serviceSearchConditionForm);
    SingleServiceWithCommentsDto searchSingleService(Long serviceId , Long clientId);
}
