package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.ServicePage;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceCommentsDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.CategoryRepository;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.service.ServiceSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.example.servicehub.domain.ServicePage.DEFAULT_START_PAGE;
import static com.example.servicehub.domain.ServicePage.POPULARITY;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceSearchImpl implements ServiceSearch {

    private final ServicesRepository servicesRepository;
    private final CategoryRepository categoryRepository;
    private final ClientServiceRepository clientServiceRepository;
    private final ServiceCommentsAdminister serviceCommentsAdminister;

    @Override
    public Page<PopularityServiceDto> search(ServiceSearchConditionForm serviceSearchConditionForm) {
        List<Category> categories = categoryRepository.findByNames(serviceSearchConditionForm.getCategories());
        List<Services> searchedService = servicesRepository.search(categories, serviceSearchConditionForm.getServiceName());
        return servicesRepository.findServices(searchedService,
                PageRequest.of(DEFAULT_START_PAGE,DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, POPULARITY.getName())));
    }

    @Override
    public SingleServiceWithCommentsDto searchSingleService(Long serviceId, Long clientId) {
        Services services = servicesRepository.findByIdUseFetchJoin(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스 조회입니다."));
        boolean isPossess = clientServiceRepository.existsServiceAndClientRelationship(services, clientId);
        List<ServiceCommentsDto> comments = serviceCommentsAdminister.searchComments(services.getId());
        return SingleServiceWithCommentsDto.of(services,isPossess,comments);
    }

}
