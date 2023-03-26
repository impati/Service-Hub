package com.example.servicehub.steps;

import com.example.servicehub.domain.ServiceComment;
import com.example.servicehub.domain.Services;
import com.example.servicehub.repository.ServiceCommentRepository;

import java.util.List;

public class ServiceCommentsSteps {

    public final static String DEFAULT_CONTENT = "Test";
    public final static String DEFAULT_NICKNAME = "impati";

    private final ServiceCommentRepository serviceCommentRepository;

    public ServiceCommentsSteps(ServiceCommentRepository serviceCommentRepository) {
        this.serviceCommentRepository = serviceCommentRepository;
    }

    public ServiceComment create(Long clientId, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(DEFAULT_CONTENT, services, clientId, DEFAULT_NICKNAME));
    }

    public ServiceComment create(String content, Long clientId, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(content, services, clientId, DEFAULT_NICKNAME));
    }


    public ServiceComment create(String content, Long clientId, String nickname, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(content, services, clientId, nickname));
    }

    public List<ServiceComment> findAllComments() {
        return serviceCommentRepository.findAll();
    }
}



