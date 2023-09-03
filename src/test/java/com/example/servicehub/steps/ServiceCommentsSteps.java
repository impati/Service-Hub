package com.example.servicehub.steps;

import java.util.List;

import com.example.servicehub.domain.services.ServiceComment;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.repository.services.ServiceCommentRepository;

public class ServiceCommentsSteps {

	public final static String DEFAULT_CONTENT = "Test";
	public final static String DEFAULT_NICKNAME = "impati";

	private final ServiceCommentRepository serviceCommentRepository;

	public ServiceCommentsSteps(final ServiceCommentRepository serviceCommentRepository) {
		this.serviceCommentRepository = serviceCommentRepository;
	}

	public ServiceComment create(final Long customerId, final Services services) {
		return serviceCommentRepository.save(
			ServiceComment.of(
				DEFAULT_CONTENT,
				services,
				customerId,
				DEFAULT_NICKNAME
			));
	}

	public ServiceComment create(
		final String content,
		final Long customerId,
		final Services services
	) {
		return serviceCommentRepository.save(ServiceComment.of(content, services, customerId, DEFAULT_NICKNAME));
	}

	public ServiceComment create(
		final String content,
		final Long customerId,
		final String nickname,
		final Services services
	) {
		return serviceCommentRepository.save(ServiceComment.of(content, services, customerId, nickname));
	}

	public List<ServiceComment> findAllComments() {
		return serviceCommentRepository.findAll();
	}
}



