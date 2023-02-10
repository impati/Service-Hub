package com.example.servicehub.service;

import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import org.springframework.data.domain.Page;

public interface ClientServiceAdminister {

    void addClientService(Long clientId , Long serviceId);

    void deleteClientService(Long clientId,Long serviceId);

    Page<ClickServiceDto>  servicesOfClient(Long clientId, ServiceSearchConditionForm serviceSearchConditionForm);

    String countClickAndReturnUrl(Long clientId,Long serviceId);

}
