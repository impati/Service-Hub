package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ClientService;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ClientServiceRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ClientServiceAdminister;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.example.servicehub.domain.ServicePage.CLICK;
import static com.example.servicehub.domain.ServicePage.DEFAULT_START_PAGE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientServiceAdministerImpl implements ClientServiceAdminister {

    private final ClientServiceRepository clientServiceRepository;
    private final ClientRepository clientRepository;
    private final ServicesRepository servicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Override
    @Transactional
    public void addClientService(Long clientId, Long serviceId) {
        ClientAndService clientAndService = createBy(clientId,serviceId);

        if(alreadyExistForClient(clientAndService.getClient(),clientAndService.getServices()))return ;

        clientServiceRepository.save(ClientService.of(clientAndService.getClient(),clientAndService.getServices()));
    }

    private boolean alreadyExistForClient(Client client ,Services services){
        if(clientServiceRepository.alreadyExistsServiceForClient(client,services))return true;
        return false;
    }

    @Override
    @Transactional
    public void deleteClientService(Long clientId, Long serviceId) {

        ClientAndService clientAndService = createBy(clientId,serviceId);

        Optional<ClientService> optionalClientService = clientServiceRepository
                .findClientServiceByClientAndServices(clientAndService.getClient(),clientAndService.getServices());

        optionalClientService.ifPresent(clientServiceRepository::delete);

    }


    @Override
    public Page<ClickServiceDto> servicesOfClient(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm) {

        Page<ClickServiceDto> servicesWithClick = servicesRepository.searchByClient(clientId, serviceSearchConditionForm.getCategories(), serviceSearchConditionForm.getServiceName(),
                PageRequest.of(DEFAULT_START_PAGE, 30, Sort.by(Sort.Direction.DESC, CLICK.getName())));

        for(var service : servicesWithClick.getContent()){
            service.setCategories(serviceCategoryRepository.findByServiceName(service.getServiceName()));
        }

        return servicesWithClick;
    }

    @Override
    @Transactional
    public String countClickAndReturnUrl(Long clientId, Long serviceId) {

        ClientAndService clientAndService = createBy(clientId,serviceId);

        ClientService clientService = clientServiceRepository.findClientServiceByClientAndServices(clientAndService.getClient(), clientAndService.getServices())
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 메서드 호출입니다."));

        clientService.click();

        return clientService.getServices().getServiceUrl();
    }

    private ClientAndService createBy(Long clientId,Long serviceId){

        Client client = clientRepository.findById(clientId)
                .orElseThrow(()->new EntityNotFoundException("유효하지 않은 사용자입니다."));

        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(()->new EntityNotFoundException("유효하지 않은 서비스입니다."));

        return new ClientAndService(client,services);
    }

    @Data
    @AllArgsConstructor
    private static class ClientAndService{
        private Client client;
        private Services services;
    }
}
