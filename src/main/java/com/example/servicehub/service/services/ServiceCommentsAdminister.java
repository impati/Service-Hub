package com.example.servicehub.service.services;


import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.dto.services.ServiceCommentsDto;

import java.util.List;

public interface ServiceCommentsAdminister {

    void addServiceComment(ServiceCommentForm commentsForm);

    void updateServiceComment(ServiceCommentUpdateForm serviceCommentUpdateForm);

    void deleteServiceComment(Long serviceCommentsId, Long customerId);

    List<ServiceCommentsDto> searchComments(Long serviceId);

    String bringCommentContent(Long serviceCommentsId);

}
