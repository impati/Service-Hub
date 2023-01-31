package com.example.servicehub.repository.querydsl;

import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.PopularityServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceSearchRepository {

    Page<PopularityServiceDto> search(List<String> categories , String serviceName,Pageable pageable);

    Page<ClickServiceDto> searchByClient(Long clientId , List<String> categories , String serviceName, Pageable pageable);

}
