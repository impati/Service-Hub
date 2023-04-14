package com.example.servicehub.service.impl;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void givenCommentsForm_whenAddingComment_thenAddComment() throws Exception {
        // given

        Services services = servicesSteps.create();
        String comment = "good";
        String nickname = "impati";
        ServiceCommentForm serviceCommentForm = new ServiceCommentForm(services.getId(), comment, 1L, nickname);
        // when
        serviceCommentsAdminister.addServiceComment(serviceCommentForm);
        // then
        List<ServiceComment> response = serviceCommentsSteps.findAllComments();
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getNickname()).isEqualTo(nickname);
        assertThat(response.get(0).getContent()).isEqualTo(comment);
    }

    @Test
    @DisplayName("서비스 댓글 수정하기")
    public void givenCommentUpdateForm_whenUpdatingComments_thenUpdateComment() throws Exception {
        // given
        Long customerId = 1L;
        Services services = servicesSteps.create();
        ServiceComment serviceComment = serviceCommentsSteps.create(customerId, services);

        String updateComment = "good";
        ServiceCommentUpdateForm serviceCommentUpdateForm =
                new ServiceCommentUpdateForm(serviceComment.getId(), customerId, updateComment);
        // when
        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);
        // then

        assertThat(serviceComment.getContent()).isEqualTo(updateComment);
    }

    @Test
    @DisplayName("서비스 댓글 삭제하기")
    public void givenComment_whenDeletingComment_thenDeleteComment() throws Exception {
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
    public void givenCommentAndUnAuthorizecustomer_whenUpdating_thenNothing() throws Exception {
        // given
        Long customerId = 1L;
        String originComment = "origin";
        Services services = servicesSteps.create();
        ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);
        String updateComment = "good";
        ServiceCommentUpdateForm updateForm =
                new ServiceCommentUpdateForm(serviceComment.getId(), 99L, updateComment);
        //when

        serviceCommentsAdminister.updateServiceComment(updateForm);

        //then
        assertThat(serviceComment.getContent())
                .isEqualTo(originComment);
    }

    @Test
    @DisplayName("사용자가 작성한 댓글이 아닌 경우 삭제 할 수 없다")
    public void givenCommentAndUnAuthorizecustomer_whenDeleting_thenNothing() throws Exception {
        // given
        Long customerId = 1L;
        String originComment = "origin";
        Services services = servicesSteps.create();
        ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);
        //when

        serviceCommentsAdminister.deleteServiceComment(serviceComment.getId(), 99L);

        //then
        assertThat(serviceCommentsSteps.findAllComments().size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 내용 조회")
    public void givenServiceCommentId_whenBringContent_thenBringContent() throws Exception {
        // given
        Long customerId = 1L;
        String originComment = "origin";
        Services services = servicesSteps.create();
        ServiceComment serviceComment = serviceCommentsSteps.create(originComment, customerId, services);
        // when
        String comment = serviceCommentsAdminister.bringCommentContent(serviceComment.getId());
        // then
        assertThat(comment).isEqualTo(originComment);
    }

    @Test
    @DisplayName("서비스의 댓글 모두 조회")
    public void given_when_then() throws Exception {
        // given
        Long customerId = 1L;
        String originComment = "origin";
        Services services = servicesSteps.create();
        String baseNickname = "base";
        for (int i = 0; i < 10; i++) {
            serviceCommentsSteps.create(originComment, customerId, baseNickname + i, services);
        }
        // when
        List<ServiceCommentsDto> response = serviceCommentsAdminister.searchComments(services.getId());
        // then
        assertThat(response.size())
                .isEqualTo(10);

        assertThat(response.get(5).getNickname())
                .isEqualTo(baseNickname + 5);
    }
}