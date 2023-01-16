package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.ServiceSortType;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.PopularityService;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.CategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceSearchImpl implements ServiceSearch {

    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int START_PAGE = 0;
    private final ServicesRepository servicesRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<PopularityService> search(ServiceSearchConditionForm serviceSearchConditionForm) {
        List<Category> categories = categoryRepository.findByNames(serviceSearchConditionForm.getCategories());
        List<Services> searchedService = servicesRepository.search(categories, serviceSearchConditionForm.getServiceName());
        return servicesRepository.findServicesSortedByPopularity(searchedService,
                PageRequest.of(START_PAGE,DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, ServiceSortType.POPULARITY.getName())));
    }

}
