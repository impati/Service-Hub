package com.example.servicehub.service;

public interface CustomerServiceAdminister {

    void addClientService(Long clientId, Long serviceId);

    void deleteClientService(Long clientId, Long serviceId);
}
