package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Client;
import com.example.servicehub.dto.ClientEditForm;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.service.ClientAdminister;
import com.example.servicehub.support.ProfileManager;
import com.example.servicehub.util.ProjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientAdministerImpl implements ClientAdminister {

    private final ClientRepository clientRepository;
    private final ProfileManager profileManager;

    @Override
    public Client findClientByClientId(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void editClientProfile(Long clientId, ClientEditForm clientEditForm) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(EntityNotFoundException::new);

        String storeName = profileManager.tryToRestore(clientEditForm.getProfileImage());

        client.update(clientEditForm.getNickname(),
                clientEditForm.getIntroComment(),
                clientEditForm.getBlogUri(),
                convertImageUrl(client,storeName));
    }

    private String convertImageUrl(Client client , String storeName){
        if(storeName.equals(ProfileManager.DEFAULT) && client.getProfileImageUrl() != null) return client.getProfileImageUrl();
        return ProjectUtils.getDomain() + "/file/profile/" + storeName;
    }

}
