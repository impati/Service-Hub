package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.domain.RequestStatus;
import com.example.servicehub.dto.RequestServiceArticleSearchCondition;
import com.example.servicehub.repository.RequestServiceArticleRepository;
import com.example.servicehub.repository.querydsl.RequestServiceArticleFinder;
import com.example.servicehub.steps.RequestServiceArticleSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({DefaultRequestServiceArticleSearch.class, TestJpaConfig.class, RequestServiceArticleFinder.class})
class DefaultRequestServiceArticleSearchTest {

    @Autowired
    private RequestServiceArticleRepository requestServiceArticleRepository;
    @Autowired
    private RequestServiceArticleSearch requestServiceArticleSearch;
    private RequestServiceArticleSteps requestServiceArticleSteps;


    @BeforeEach
    void setup() {
        requestServiceArticleSteps = new RequestServiceArticleSteps(requestServiceArticleRepository);
    }

    @Test
    @DisplayName("RequestServiceArticle 단일 조회 테스트")
    public void givenId_whenSearchingArticle_thenReturnArticle() throws Exception {
        // given
        RequestServiceArticle requestServiceArticle = requestServiceArticleSteps.create();

        // when
        RequestServiceArticle response = requestServiceArticleSearch.searchSingleArticle(requestServiceArticle.getId());

        // then
        assertThat(response.getId()).isEqualTo(requestServiceArticle.getId());
        assertThat(response.getServiceContent()).isEqualTo(requestServiceArticle.getServiceContent());
    }

    @Test
    @DisplayName("RequestServiceArticle 조회 테스트")
    public void givenSearchCondition_whenSearching_thenReturnRequestServiceArticleResultFitTheCondition() throws Exception {
        // given
        requestServiceArticleSteps.create();

        RequestServiceArticleSearchCondition condition = new RequestServiceArticleSearchCondition(RequestStatus.BEFORE, null);

        // when
        Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(condition, PageRequest.of(0, 10));

        // then
        assertThat(response.getContent().size()).isEqualTo(1);
    }

    @ParameterizedTest(name = "#[{index}]  merge = [{0}] , nickname = [{1}]")
    @MethodSource("searchArticle")
    @DisplayName("RequestServiceArticle 여러 개 조회 테스트")
    public void givenSearchCondition_whenSearching_thenReturnMultipleRequestServiceArticleResultFitTheCondition(
            RequestStatus requestStatus, String nickname, int pageSize, int numberOfElements, int totalElements, int totalPages, int pageNumber
    ) throws Exception {
        // given
        int n = 10;
        for (int i = 0; i < n; i++) requestServiceArticleSteps.createRandom("test1", 99L);
        for (int i = 0; i < 2 * n; i++) requestServiceArticleSteps.createRandom("test2", 999L);
        for (int i = 0; i < 2 * n; i++) requestServiceArticleSteps.createRandomAndMerge("test2", 999L);

        RequestServiceArticleSearchCondition condition = new RequestServiceArticleSearchCondition(requestStatus, nickname);

        // when
        Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(condition, PageRequest.of(pageNumber, pageSize));

        // then
        assertThat(response.getNumberOfElements()).isEqualTo(numberOfElements);
        assertThat(response.getTotalElements()).isEqualTo(totalElements);
        assertThat(response.getTotalPages()).isEqualTo(totalPages);
        assertThat(response.getNumber()).isEqualTo(pageNumber);

    }

    private static Stream<Arguments> searchArticle() {
        return Stream.of(
                Arguments.of(null, null, setPageSize(10), setNumberOfElements(10), setTotalElements(50), setTotalPages(50 / 10), setPageNumber(0)),
                Arguments.of(RequestStatus.BEFORE, null, setPageSize(10), setNumberOfElements(10), setTotalElements(30), setTotalPages(30 / 10), setPageNumber(0)),
                Arguments.of(RequestStatus.BEFORE, null, setPageSize(12), setNumberOfElements(12), setTotalElements(30), setTotalPages(30 / 12 + 1), setPageNumber(0)),
                Arguments.of(RequestStatus.COMPLETE, null, setPageSize(12), setNumberOfElements(12), setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(0)),
                Arguments.of(RequestStatus.COMPLETE, null, setPageSize(12), setNumberOfElements(20 % 12), setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(20 / 12)),
                Arguments.of(null, "test1", setPageSize(12), setNumberOfElements(10), setTotalElements(10), setTotalPages(1), setPageNumber(0)),
                Arguments.of(null, "test2", setPageSize(12), setNumberOfElements(12), setTotalElements(40), setTotalPages(40 / 12 + 1), setPageNumber(0)),
                Arguments.of(RequestStatus.BEFORE, "test2", setPageSize(12), setNumberOfElements(12), setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(0)),
                Arguments.of(RequestStatus.COMPLETE, "test2", setPageSize(12), setNumberOfElements(12), setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(0))
        );
    }

    private static int setPageSize(int pageSize) {
        return pageSize;
    }

    private static int setNumberOfElements(int numberOfElements) {
        return numberOfElements;
    }


    private static int setTotalPages(int totalPages) {
        return totalPages;
    }

    private static int setTotalElements(int totalElements) {
        return totalElements;
    }

    private static int setPageNumber(int pageNumber) {
        return pageNumber;
    }


}