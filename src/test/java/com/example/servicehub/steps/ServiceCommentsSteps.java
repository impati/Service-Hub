package com.example.servicehub.steps;

import com.example.servicehub.domain.services.ServiceComment;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServiceCommentRepository;

import java.util.List;

public class ServiceCommentsSteps {

    public final static String DEFAULT_CONTENT = "Test";
    public final static String DEFAULT_NICKNAME = "impati";

    private final ServiceCommentRepository serviceCommentRepository;

    public ServiceCommentsSteps(ServiceCommentRepository serviceCommentRepository) {
        this.serviceCommentRepository = serviceCommentRepository;
    }

    public ServiceComment create(Long customerId, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(DEFAULT_CONTENT, services, customerId, DEFAULT_NICKNAME));
    }

    public ServiceComment create(String content, Long customerId, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(content, services, customerId, DEFAULT_NICKNAME));
    }


    public ServiceComment create(String content, Long customerId, String nickname, Services services) {
        return serviceCommentRepository.save(ServiceComment.of(content, services, customerId, nickname));
    }

    public List<ServiceComment> findAllComments() {
        return serviceCommentRepository.findAll();
    }
}



