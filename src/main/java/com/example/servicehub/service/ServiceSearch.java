package com.example.servicehub.service;

import com.example.servicehub.dto.PopularityService;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import org.springframework.data.domain.Page;


public interface ServiceSearch {
    Page<PopularityService> search(ServiceSearchConditionForm serviceSearchConditionForm);
}
