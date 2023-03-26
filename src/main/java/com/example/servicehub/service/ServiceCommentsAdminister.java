package com.example.servicehub.service;


import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.dto.ServiceCommentsDto;

import java.util.List;

public interface ServiceCommentsAdminister {

    void addServiceComment(ServiceCommentForm commentsForm);

    void updateServiceComment(ServiceCommentUpdateForm serviceCommentUpdateForm);

    void deleteServiceComment(Long serviceCommentsId, Long clientId);

    List<ServiceCommentsDto> searchComments(Long serviceId);

    String bringCommentContent(Long serviceCommentsId);

}
