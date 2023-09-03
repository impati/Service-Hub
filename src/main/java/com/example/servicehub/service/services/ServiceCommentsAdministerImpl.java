package com.example.servicehub.service.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.services.ServiceComment;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.dto.services.ServiceCommentsDto;
import com.example.servicehub.repository.services.ServiceCommentRepository;
import com.example.servicehub.repository.services.ServicesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceCommentsAdministerImpl implements ServiceCommentsAdminister {

	private final ServiceCommentRepository serviceCommentRepository;
	private final ServicesRepository servicesRepository;

	@Override
	@Transactional
	public void addServiceComment(final ServiceCommentForm commentsForm) {
		final Services services = servicesRepository.findById(commentsForm.getServiceId())
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않는 서비스 입니다"));

		serviceCommentRepository.save(ServiceComment.of(
			commentsForm.getContent(), services, commentsForm.getCustomerId(), commentsForm.getNickname()));
	}

	@Override
	@Transactional
	public void updateServiceComment(final ServiceCommentUpdateForm serviceCommentUpdateForm) {
		final ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentUpdateForm.getCommentId())
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 댓글을 수정 시도했습니다"));

		if (isAuthorizeToChangeComments(serviceComment, serviceCommentUpdateForm.getCustomerId())) {
			serviceComment.updateContent(serviceCommentUpdateForm.getContent());
		}
	}

	@Override
	@Transactional
	public void deleteServiceComment(final Long serviceCommentsId, final Long clientId) {
		final ServiceComment serviceComment = serviceCommentRepository.findById(serviceCommentsId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 댓글을 수정 시도했습니다"));

		if (isAuthorizeToChangeComments(serviceComment, clientId)) {
			serviceCommentRepository.delete(serviceComment);
		}
	}

	private boolean isAuthorizeToChangeComments(final ServiceComment serviceComment, final Long clientId) {
		return serviceCommentRepository.existsByCustomer(serviceComment, clientId);
	}

	@Override
	public List<ServiceCommentsDto> searchComments(final Long serviceId) {
		return serviceCommentRepository.findByServices(serviceId).stream()
			.map(ServiceCommentsDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public String bringCommentContent(final Long serviceCommentsId) {
		return serviceCommentRepository.findById(serviceCommentsId)
			.orElseThrow(() -> new EntityNotFoundException("유효하지 않은 댓글 접근 입니다."))
			.getContent();
	}
}
