package com.example.servicehub.service.impl;

import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.service.CustomServiceAdminister;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientServiceAdministerImpl implements ClientServiceAdminister {

    private final ClientServiceRepository clientServiceRepository;
    private final ServicesRepository servicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final CustomServiceAdminister customServiceAdminister;

    @Override
    @Transactional
    public void addClientService(Long clientId, Long serviceId) {
        Services services = findService(serviceId);
        if (alreadyExistForClient(clientId, services)) return;
        clientServiceRepository.save(ClientService.of(clientId, services));
    }

    private boolean alreadyExistForClient(Long clientId, Services services) {
        return clientServiceRepository.alreadyExistsServiceForClient(clientId, services);
    }

    @Override
    @Transactional
    public void deleteClientService(Long clientId, Long serviceId) {
        Services services = findService(serviceId);
        Optional<ClientService> optionalClientService = clientServiceRepository.findClientServiceByClientIdAndServices(clientId, services);
        optionalClientService.ifPresent(clientServiceRepository::delete);
    }


    @Override
    public List<ClickServiceDto> servicesOfClient(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm) {

        List<ClickServiceDto> servicesWithClick = firstSearchCustomServices(clientId, serviceSearchConditionForm);

        List<ClickServiceDto> services = secondSearchServices(clientId, serviceSearchConditionForm);

        servicesWithClick.addAll(services);

        return servicesWithClick;
    }


    private List<ClickServiceDto> firstSearchCustomServices(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm) {
        List<ClickServiceDto> servicesWithClick = new ArrayList<>();

        if (isCustomSearch(serviceSearchConditionForm.getCategories())) {
            servicesWithClick.addAll(customServiceAdminister.customServicesOfClient(clientId, serviceSearchConditionForm.getServiceName())
                    .stream()
                    .map(ClickServiceDto::from)
                    .collect(Collectors.toList()));
        }

        return servicesWithClick;
    }

    private List<ClickServiceDto> secondSearchServices(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm) {
        List<ClickServiceDto> services = servicesRepository.searchByClient(clientId, serviceSearchConditionForm.getCategories(), serviceSearchConditionForm.getServiceName());

        for (var service : services) {
            service.setCategories(serviceCategoryRepository.findByServiceName(service.getServiceName()));
        }

        return services;
    }

    private boolean isCustomSearch(List<String> categories) {
        if (categories == null) return true;
        if (categories.isEmpty()) return true;
        return categories.contains("CUSTOM");
    }

    @Override
    @Transactional
    public String countClickAndReturnUrl(Long clientId, Long serviceId) {

        ClientService clientService = clientServiceRepository.findClientServiceByClientIdAndServices(clientId, findService(serviceId))
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 메서드 호출입니다."));

        clientService.click();

        return clientService.getServices().getServiceUrl();
    }

    private Services findService(Long serviceId) {
        return servicesRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));
    }

    private ClientAndService createBy(Long clientId, Long serviceId) {

        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 서비스입니다."));

        return new ClientAndService(clientId, services);
    }

    @Data
    @AllArgsConstructor
    private static class ClientAndService {
        private Long clientId;
        private Services services;
    }
}
