package com.example.servicehub.service.impl;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ServiceComment;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.dto.ServiceCommentsDto;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ServiceCommentRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceCommentsAdminister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ServiceCommentsAdministerImpl implements ServiceCommentsAdminister {

    private final ServiceCommentRepository serviceCommentRepository;
    private final ServicesRepository servicesRepository;
    private final ClientRepository clientRepository;

    @Override
    public void addServiceComment(ServiceCommentForm commentsForm) {
        Services services = servicesRepository
                .findById(commentsForm.getServiceId()).orElseThrow(()-> new EntityNotFoundException("유효하지 않는 서비스 입니다"));

        Client client = clientRepository
                .findById(commentsForm.getClientId()).orElseThrow(()-> new EntityNotFoundException("유효하지 않은 사용자입니다."));

        serviceCommentRepository.save(ServiceComment.of(
                commentsForm.getContent(),services,client));
    }

    @Override
    public void updateServiceComment(ServiceCommentUpdateForm serviceCommentUpdateForm) {
        ServiceComment serviceComment  = serviceCommentRepository.findById(serviceCommentUpdateForm.getCommentId())
                .orElseThrow(()-> new EntityNotFoundException("유효하지 않은 댓글을 수정 시도했습니다"));

        if(isAuthorizeToChangeComments(serviceComment,serviceCommentUpdateForm.getClientId()))
            serviceComment.updateContent(serviceCommentUpdateForm.getContent());
    }

    @Override
    public void deleteServiceComment(Long serviceCommentsId, Long clientId) {
        ServiceComment serviceComment  = serviceCommentRepository.findById(serviceCommentsId)
                .orElseThrow(()-> new EntityNotFoundException("유효하지 않은 댓글을 수정 시도했습니다"));

        if(isAuthorizeToChangeComments(serviceComment,clientId))
            serviceCommentRepository.delete(serviceComment);
    }

    private boolean isAuthorizeToChangeComments(ServiceComment serviceComment , Long clientId){
        if(serviceCommentRepository.existsByClient(serviceComment,clientId)) return true;
        return false;
    }

    @Override
    public List<ServiceCommentsDto> searchComments(Long serviceId) {
        return serviceCommentRepository
                .findByServices(serviceId)
                .stream()
                .map(ServiceCommentsDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public String getCommentContent(Long serviceCommentsId) {
        return serviceCommentRepository
                .findById(serviceCommentsId)
                .orElseThrow(()-> new EntityNotFoundException("유효하지 않은 댓글 접근 입니다."))
                .getContent();
    }


}
