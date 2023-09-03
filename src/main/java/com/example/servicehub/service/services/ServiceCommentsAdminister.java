package com.example.servicehub.service.services;

import java.util.List;

import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.dto.services.ServiceCommentsDto;

public interface ServiceCommentsAdminister {

	void addServiceComment(final ServiceCommentForm commentsForm);

	void updateServiceComment(final ServiceCommentUpdateForm serviceCommentUpdateForm);

	void deleteServiceComment(final Long serviceCommentsId, final Long customerId);

	List<ServiceCommentsDto> searchComments(final Long serviceId);

	String bringCommentContent(final Long serviceCommentsId);
}
