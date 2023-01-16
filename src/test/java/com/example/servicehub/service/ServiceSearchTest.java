package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.dto.PopularityServiceDto;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.service.impl.ServiceSearchImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("서비스 검색 테스트")
@DataJpaTest
@Import({TestJpaConfig.class, ServiceSearchImpl.class})
class ServiceSearchTest {

    @Autowired private ServiceSearch serviceSearch;

    @Test
    @DisplayName("카테고리로 검색 - 단일")
    public void givenCategory_whenSearchingService_thenReturnFitTheConditionServices() throws Exception{
        // given
        ServiceSearchConditionForm serviceSearchConditionForm =
                ServiceSearchConditionForm.of(List.of("search-platform"),null);

        // when
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);

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
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);
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
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);
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
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);
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
        Page<PopularityServiceDto> searchedServices = serviceSearch.search(serviceSearchConditionForm);
        // then
        checkContainKeyword(searchedServices,"깃허브");
        checkContainKeyword(searchedServices,"유튜브");
    }


    private void checkContainKeyword(Page<PopularityServiceDto> searchedServices, String keyword){
        assertThat(searchedServices.stream()
                .filter(services -> services.getServiceName().contains(keyword))
                .findFirst()).isPresent();
    }
}