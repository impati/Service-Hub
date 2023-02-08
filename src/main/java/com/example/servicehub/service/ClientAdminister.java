package com.example.servicehub.service;

import com.example.servicehub.domain.Client;
import com.example.servicehub.dto.ClientEditForm;

public interface ClientAdminister {

    Client findClientByClientId(Long clientId);

    void editClientProfile(Long clientId,ClientEditForm clientEditForm);
}
