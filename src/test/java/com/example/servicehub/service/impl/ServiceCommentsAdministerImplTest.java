package com.example.servicehub.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.servicehub.config.StepsConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.services.ServiceComment;
import com.example.servicehub.domain.services.Services;
import com.example.servicehub.dto.services.ServiceCommentForm;
import com.example.servicehub.dto.services.ServiceCommentUpdateForm;
import com.example.servicehub.dto.services.ServiceCommentsDto;
import com.example.servicehub.service.services.ServiceCommentsAdminister;
import com.example.servicehub.service.services.ServiceCommentsAdministerImpl;
import com.example.servicehub.steps.ServiceCommentsSteps;
import com.example.servicehub.steps.ServicesSteps;

@DataJpaTest
@DisplayName("댓글 기능 구현 테스트")
@Import({TestJpaConfig.class, StepsConfig.class, ServiceCommentsAdministerImpl.class})
class ServiceCommentsAdministerImplTest {

	@Autowired
	private ServiceCommentsAdminister serviceCommentsAdminister;

	@Autowired
	private ServicesSteps servicesSteps;

	@Autowired
	private ServiceCommentsSteps serviceCommentsSteps;

	@Test
	@DisplayName("서비스에 댓글 추가하기")
	void givenCommentsForm_whenAddingComment_thenAddComment() {
		// given
		final Services services = servicesSteps.create();
		final String comment = "good";
		final String nickname = "impati";
		final ServiceCommentForm serviceCommentForm = new ServiceCommentForm(services.getId(), comment, 1L, nickname);

		// when
		serviceCommentsAdminister.addServiceComment(serviceCommentForm);

		// then
		final List<ServiceComment> response = serviceCommentsSteps.findAllComments();
		assertThat(response)
			.hasSize(1)
			.extracting(ServiceComment::getNickname, ServiceComment::getContent)
			.contains(tuple(nickname, comment));
	}

	@Test
	@DisplayName("서비스 댓글 수정하기")
	void givenCommentUpdateForm_whenUpdatingComments_thenUpdateComment() {
		// given
		final Long customerId = 1L;
		final Services services = servicesSteps.create();
		final ServiceComment serviceComment = serviceCommentsSteps.create(customerId, services);
		final String updateComment = "good";
		final ServiceCommentUpdateForm serviceCommentUpdateForm = new ServiceCommentUpdateForm(
			serviceComment.getId(),
			customerId,
			updateComment
		);

		// when
		serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);

		// then
		assertThat(serviceComment.getContent()).isEqualTo(updateComment);
	}

	@Test
	@DisplayName("서비스 댓글 삭제하기")
	void givenComment_whenDeletingComment_thenDeleteComment() {
		// given
		Long customerId = 1L;
		Services services = servicesSteps.create();
		ServiceComment serviceComment = serviceCommentsSteps.create(customerId, services);
		// when
		serviceCommentsAdminister.deleteServiceComment(serviceComment.getId(), 1L);
		// then
		assertThat(serviceCommentsSteps.findAllComments().size())
			.isEqualTo(0);
	}

	@Test
	@DisplayName("사용자가 작성한 댓글이 아닌 경우 수정 할 수 없다")
	void givenCommentAndUnAuthorizecustomer_whenUpdating_thenNothing() {
		// given
		final Long customerId = 1L;
		final String originComment = "origin";
		final Services services = servicesSteps.create();
		final ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);
		final String updateComment = "good";
		final ServiceCommentUpdateForm updateForm = new ServiceCommentUpdateForm(
			serviceComment.getId(),
			99L,
			updateComment
		);

		//when
		serviceCommentsAdminister.updateServiceComment(updateForm);

		//then
		assertThat(serviceComment.getContent()).isEqualTo(originComment);
	}

	@Test
	@DisplayName("사용자가 작성한 댓글이 아닌 경우 삭제 할 수 없다")
	void givenCommentAndUnAuthorizecustomer_whenDeleting_thenNothing() {
		// given
		final Long customerId = 1L;
		final String originComment = "origin";
		final Services services = servicesSteps.create();
		final ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);

		//when
		serviceCommentsAdminister.deleteServiceComment(serviceComment.getId(), 99L);

		//then
		assertThat(serviceCommentsSteps.findAllComments()).hasSize(1);
	}

	@Test
	@DisplayName("댓글 내용 조회")
	void givenServiceCommentId_whenBringContent_thenBringContent() {
		// given
		final Long customerId = 1L;
		final String originComment = "origin";
		final Services services = servicesSteps.create();
		final ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);

		// when
		final String comment = serviceCommentsAdminister.bringCommentContent(serviceComment.getId());

		// then
		assertThat(comment).isEqualTo(originComment);
	}

	@Test
	@DisplayName("서비스의 댓글 모두 조회")
	void findAllServiceComment() {
		// given
		final Long customerId = 1L;
		final String originComment = "origin";
		final Services services = servicesSteps.create();
		final String baseNickname = "base";
		for (int i = 0; i < 10; i++) {
			serviceCommentsSteps.create(originComment, customerId, baseNickname + i, services);
		}

		// when
		final List<ServiceCommentsDto> response = serviceCommentsAdminister.searchComments(services.getId());

		// then
		assertThat(response).hasSize(10);
	}
}
