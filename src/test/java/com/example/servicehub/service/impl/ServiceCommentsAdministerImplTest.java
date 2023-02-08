package com.example.servicehub.service.impl;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.domain.ServiceComment;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServiceCommentForm;
import com.example.servicehub.dto.ServiceCommentUpdateForm;
import com.example.servicehub.dto.ServiceCommentsDto;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ServiceCommentRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.ServiceCommentsAdminister;
import com.example.servicehub.util.ProjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("댓글 기능 구현 테스트")
@Import({TestJpaConfig.class,ServiceCommentsAdministerImpl.class})
class ServiceCommentsAdministerImplTest {

    @Autowired private ServiceCommentsAdminister serviceCommentsAdminister;
    @Autowired private ServiceCommentRepository serviceCommentRepository;
    @Autowired private ServicesRepository servicesRepository;
    @Autowired private ClientRepository clientRepository;

    @Test
    @DisplayName("서비스에 댓글 추가하기")
    public void givenCommentsForm_whenAddingComment_thenAddComment() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        String content = "노션 진짜 좋아요";
        ServiceCommentForm serviceCommentForm = new ServiceCommentForm(services.getId(), 1L, content);
        // when
        serviceCommentsAdminister.addServiceComment(serviceCommentForm);
        // then
        assertThat(
        serviceCommentRepository.findByServices(services.getId())
                .stream()
                .filter(serviceComment -> serviceComment.getContent().equals(content))
                .count()).isGreaterThan(0);
    }

    @Test
    @DisplayName("서비스 댓글 수정하기")
    public void givenCommentUpdateForm_whenUpdatingComments_thenUpdateComment() throws Exception{
        // given
        Client client = clientRepository.findById(1L).get();
        String content = "노션 진짜 좋음";
        ServiceCommentUpdateForm serviceCommentUpdateForm =
                new ServiceCommentUpdateForm(1L,client.getId(),content);

        // when
        serviceCommentsAdminister.updateServiceComment(serviceCommentUpdateForm);
        // then
        assertThat(
                serviceCommentRepository.findByClient(client)
                        .stream()
                        .filter(serviceComment -> serviceComment.getContent().equals(content))
                        .count()).isGreaterThan(0);
    }

    @Test
    @DisplayName("서비스 댓글 삭제하기")
    public void givenComment_whenDeletingComment_thenDeleteComment() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        String content = "노션 진짜 좋아요";
        serviceCommentsAdminister.addServiceComment(new ServiceCommentForm(services.getId(), 1L, content));

        ServiceComment comment = serviceCommentRepository.findByServices(services.getId())
                .stream()
                .filter(serviceComment -> serviceComment.getContent().equals(content))
                .findFirst()
                .get();
        // when
        serviceCommentsAdminister.deleteServiceComment(comment.getId(),1L);
        serviceCommentRepository.flush();
        // then
        assertThatCode(()->serviceCommentRepository.findById(comment.getId()).orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("사용자가 작성한 댓글이 아닌 경우 수정도 삭제도 할 수 없다")
    public void givenCommentAndUnAuthorizeClient_whenUpdatingAndDeleting_thenNothing() throws Exception{
        // given
        ServiceComment serviceComment = serviceCommentRepository.findById(1L).get();
        Client newClient = Client.of("test","test","test","test@naver.com", "ROLE_USER", ProviderType.KEYCLOAK, ProjectUtils.getDomain() + "client",
                ProjectUtils.getDomain() +"/profile/default.png");
        clientRepository.save(newClient);
        // whenUpdating
        serviceCommentsAdminister.updateServiceComment(new ServiceCommentUpdateForm(serviceComment.getId(),newClient.getId(),"XXX"));
        // then
        assertThat(serviceCommentRepository
                .findById(serviceComment.getId()).get().getContent())
                .isNotEqualTo("XXX");


        // whenDeleting
        serviceCommentsAdminister.deleteServiceComment(serviceComment.getId(),newClient.getId());
        // then
        assertThat(serviceCommentRepository.findById(serviceComment.getId()).isPresent()).isTrue();
    }

    @Test
    @DisplayName("서비스에 달린 댓글 조회")
    public void given_when_then() throws Exception{
        // given
        for(int i = 0;i<10;i++) serviceCommentsAdminister.addServiceComment(new ServiceCommentForm(3L, 1L, "1111"));
        serviceCommentsAdminister.addServiceComment(new ServiceCommentForm(3L,2L,"414"));
        // when
        List<ServiceCommentsDto> serviceCommentsDtos = serviceCommentsAdminister.searchComments(3L);
        // then
        assertThat(serviceCommentsDtos.size()).isEqualTo(11);
        assertThat(serviceCommentsDtos
                .stream()
                .filter(element->element.getClientId().equals(1L))
                .count()).isEqualTo(10);
    }
}