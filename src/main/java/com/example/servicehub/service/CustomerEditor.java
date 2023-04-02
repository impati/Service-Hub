package com.example.servicehub.service;

import com.example.servicehub.config.CustomerServer;
import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.support.ProfileManager;
import com.example.servicehub.util.ProjectUtils;
import com.example.servicehub.web.dto.CustomerEditForm;
import com.example.servicehub.web.dto.CustomerEditRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CustomerEditor {
    private static final String editUri = "/api/v1/customer";
    private final CustomerServer customerServer;
    private final ProfileManager profileManager;
    private final RestTemplate restTemplate;

    public void edit(CustomerEditForm customerEditForm) {
        CustomerEditRequest customerEditRequest = convertFrom(customerEditForm);
        RequestEntity<CustomerEditRequest> request = RequestEntity.patch(customerServer.getServer() + editUri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .body(customerEditRequest);
        restTemplate.exchange(request, Void.class);
        synchronization(customerEditRequest);
    }

    private CustomerEditRequest convertFrom(CustomerEditForm customerEditForm) {
        String storeName = profileManager.tryToRestore(customerEditForm.getProfileImage());
        return CustomerEditRequest.builder()
                .blogUrl(customerEditForm.getBlogUrl())
                .introduceComment(customerEditForm.getIntroComment())
                .nickname(customerEditForm.getNickname())
                .profileUrl(ProjectUtils.getDomain() + "/file/profile/" + storeName)
                .build();
    }

    private String getAccessToken() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    private void synchronization(CustomerEditRequest customerEditRequest) {
        CustomerPrincipal principal = (CustomerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        principal.setNickname(customerEditRequest.getNickname());
        principal.setBlogUrl(customerEditRequest.getBlogUrl());
        principal.setIntroduceComment(customerEditRequest.getIntroduceComment());
        principal.setProfileImageUrl(customerEditRequest.getProfileUrl());
    }
}
