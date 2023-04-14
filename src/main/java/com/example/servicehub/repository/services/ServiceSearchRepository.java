package com.example.servicehub.repository.services;

import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.PopularityServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceSearchRepository {

    Page<PopularityServiceDto> search(List<String> categories, String serviceName, Pageable pageable);

    List<ClickServiceDto> searchByCustomer(Long customerId, List<String> categories, String serviceName);

}
