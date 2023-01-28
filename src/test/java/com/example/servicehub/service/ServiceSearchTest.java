package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.dto.SingleServiceWithCommentsDto;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.repository.ServiceCategoryRepository;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.service.impl.ServiceCommentsAdministerImpl;
import com.example.servicehub.service.impl.ServiceSearchImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("서비스 검색 테스트")
@DataJpaTest
@Import({TestJpaConfig.class, ServiceSearchImpl.class, ServiceCommentsAdministerImpl.class})
class ServiceSearchTest {

    @Autowired private ServiceSearch serviceSearch;
    @Autowired private ClientRepository clientRepository;
    @Autowired private ServicesRepository servicesRepository;
    @Autowired private ServiceCategoryRepository serviceCategoryRepository;

    @Test
    @DisplayName("카테고리로 검색 - 단일")
    public void givenCategory_whenSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("search-platform"),null);

        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,Optional.empty());

        // then

        checkContainKeyword(searchedServices,"네이버");
        checkContainKeyword(searchedServices,"유튜브");
    }

    @Test
    @DisplayName("카테고리로 검색 - 멀티")
    public void givenCategories_whenSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("search-platform","IT"),null);
        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,Optional.empty());
        // then
        checkContainKeyword(searchedServices,"네이버");
        checkContainKeyword(searchedServices,"유튜브");
        checkContainKeyword(searchedServices,"노션");
        checkContainKeyword(searchedServices,"깃허브");
    }

    @Test
    @DisplayName("서비스 이름 검색 - 정확히 입력했을 때")
    public void givenServiceName_whenExactMatchSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null,"프로그래머스");
        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,Optional.empty());
        // then
        assertThat(searchedServices.getContent().get(0).getServiceName()).isEqualTo("프로그래머스");
    }

    @Test
    @DisplayName("서비스 이름 검색 - 일부 입력했을 때")
    public void givenServiceName_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null,"잡");
        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,Optional.empty());
        // then
        checkContainKeyword(searchedServices,"잡플래닛");
        checkContainKeyword(searchedServices,"잡코리아");
    }

    @Test
    @DisplayName("서비스 이름 , 카테고리 동시 검색")
    public void givenServiceNameAndCategories_whenLikeMatchSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("search-platform","IT"),"브");
        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm,Optional.empty());
        // then
        checkContainKeyword(searchedServices,"깃허브");
        checkContainKeyword(searchedServices,"유튜브");
    }

    private void checkContainKeyword(Page<PopularityServiceDto> searchedServices, String keyword){
        assertThat(searchedServices.stream()
                .filter(services -> services.getServiceName().contains(keyword))
                .findFirst()).isPresent();
    }

    @Test
    @DisplayName("서비스 조회 - 사용자 소유 여부")
    public void givenAuthenticationUser_whenSearchingServicePage_thenReturnServicesWithPossess() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(null,null);
        // when
        Page<PopularityServiceDto> searchedService = serviceSearch.search(serviceSearchConditionForm, Optional.of(1L));

        // then

        Assertions.assertThat(searchedService
                .getContent()
                .stream()
                .filter(PopularityServiceDto::isPossess)
                .count()).isEqualTo(2);
    }

    @Test
    @DisplayName("단일 서비스 조회 - 댓글이 없는 서비스 정보 조회")
    public void givenServiceId_whenSearchingServiceInformation_thenReturnServiceInformation() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        List<String> categoriesName = serviceCategoryRepository
                .findByServices(services)
                .stream()
                .map(serviceCategory -> serviceCategory.getCategory().getName())
                .collect(Collectors.toList());
        // when
        SingleServiceWithCommentsDto singleServiceWithCommentsDto = serviceSearch.searchSingleService(services.getId(),Optional.ofNullable(1L));
        // then
        assertThat(singleServiceWithCommentsDto.getServiceName())
                .isEqualTo(services.getServiceName());

        singleServiceWithCommentsDto
                .getCategories()
                .stream()
                .forEach(category->{
                    assertThat(categoriesName.contains(category)).isTrue();
                });
    }

    @Test
    @DisplayName("단일 서비스 조회 - 사용자가 서비스 소유 체크")
    public void givenServiceIdAndClientId_whenSearchingService_thenReturnsWhetherTheClientOwns() throws Exception{
        // given
        Services possessServices = servicesRepository.findById(1L).get();
        Services nonPossessServices = servicesRepository.findById(5L).get();
        Client client = clientRepository.findById(1L).get();

        // when
        SingleServiceWithCommentsDto possessSingleServiceWithCommentsDto = serviceSearch.searchSingleService(possessServices.getId(), Optional.ofNullable(client.getId()));
        SingleServiceWithCommentsDto nonPossessSingleServiceWithCommentsDto = serviceSearch.searchSingleService(nonPossessServices.getId(), Optional.ofNullable(client.getId()));
        // then

        assertThat(possessSingleServiceWithCommentsDto.isPossess()).isTrue();
        assertThat(nonPossessSingleServiceWithCommentsDto.isPossess()).isFalse();
    }

    @Test
    @DisplayName("단일 서비스 조회 - 댓글과 같이")
    public void givenServiceIdAndClientID_whenSearching_thenReturnServiceWithComments() throws Exception{
        // given
        // when
        SingleServiceWithCommentsDto singleServiceWithCommentsDto = serviceSearch.searchSingleService(1L, Optional.ofNullable(1L));
        // then
        assertThat(singleServiceWithCommentsDto.getComments().size())
                .isEqualTo(1);
    }


    @Test
    @DisplayName("단일 서비스 조회 - 인증된 사용자가 아닐 경우")
    public void givenUnAuthenticationUser_whenSearchingServicePage_thenReturnServiceWithComments() throws Exception{
        // given
        // when
        SingleServiceWithCommentsDto singleServiceWithCommentsDto = serviceSearch.searchSingleService(1L, Optional.ofNullable(null));
        // then
        assertThat(singleServiceWithCommentsDto.getComments().size())
                .isEqualTo(1);
    }


}