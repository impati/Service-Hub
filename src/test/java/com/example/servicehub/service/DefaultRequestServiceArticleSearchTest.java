package com.example.servicehub.service;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

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

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.example.servicehub.dto.requestService.RequestServiceArticleSearchCondition;
import com.example.servicehub.repository.requestService.RequestServiceArticleFinder;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;
import com.example.servicehub.service.requestService.DefaultRequestServiceArticleSearch;
import com.example.servicehub.service.requestService.RequestServiceArticleSearch;
import com.example.servicehub.steps.RequestServiceArticleSteps;

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
	void givenId_whenSearchingArticle_thenReturnArticle() {
		// given
		final RequestServiceArticle requestServiceArticle = requestServiceArticleSteps.create();

		// when
		final RequestServiceArticle response = requestServiceArticleSearch.searchSingleArticle(
			requestServiceArticle.getId());

		// then
		assertThat(response.getId()).isEqualTo(requestServiceArticle.getId());
		assertThat(response.getServiceContent()).isEqualTo(requestServiceArticle.getServiceContent());
	}

	@Test
	@DisplayName("RequestServiceArticle 조회 테스트")
	void givenSearchCondition_whenSearching_thenReturnRequestServiceArticleResultFitTheCondition() {
		// given
		requestServiceArticleSteps.create();
		final RequestServiceArticleSearchCondition condition = new RequestServiceArticleSearchCondition(
			RequestStatus.BEFORE,
			null
		);

		// when
		final Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(
			condition,
			PageRequest.of(0, 10)
		);

		// then
		assertThat(response.getContent()).hasSize(1);
	}

	@ParameterizedTest(name = "#[{index}]  merge = [{0}] , nickname = [{1}]")
	@MethodSource("searchArticle")
	@DisplayName("RequestServiceArticle 여러 개 조회 테스트")
	void givenSearchCondition_whenSearching_thenReturnMultipleRequestServiceArticleResultFitTheCondition(
		final RequestStatus requestStatus,
		final String nickname,
		final int pageSize,
		final int numberOfElements,
		final int totalElements,
		final int totalPages,
		final int pageNumber
	) {
		// given
		int n = 10;
		for (int i = 0; i < n; i++) {
			requestServiceArticleSteps.createRandom("test1", 99L);
		}

		for (int i = 0; i < 2 * n; i++) {
			requestServiceArticleSteps.createRandom("test2", 999L);
		}

		for (int i = 0; i < 2 * n; i++) {
			requestServiceArticleSteps.createRandomAndMerge("test2", 999L);
		}

		final RequestServiceArticleSearchCondition condition = new RequestServiceArticleSearchCondition(
			requestStatus,
			nickname
		);

		// when
		final Page<RequestServiceArticle> response = requestServiceArticleSearch.searchArticle(condition,
			PageRequest.of(pageNumber, pageSize));

		// then
		assertThat(response.getNumberOfElements()).isEqualTo(numberOfElements);
		assertThat(response.getTotalElements()).isEqualTo(totalElements);
		assertThat(response.getTotalPages()).isEqualTo(totalPages);
		assertThat(response.getNumber()).isEqualTo(pageNumber);
	}

	private static Stream<Arguments> searchArticle() {
		return Stream.of(
			Arguments.of(null, null, setPageSize(10), setNumberOfElements(10), setTotalElements(50),
				setTotalPages(50 / 10), setPageNumber(0)),
			Arguments.of(RequestStatus.BEFORE, null, setPageSize(10), setNumberOfElements(10), setTotalElements(30),
				setTotalPages(30 / 10), setPageNumber(0)),
			Arguments.of(RequestStatus.BEFORE, null, setPageSize(12), setNumberOfElements(12), setTotalElements(30),
				setTotalPages(30 / 12 + 1), setPageNumber(0)),
			Arguments.of(RequestStatus.COMPLETE, null, setPageSize(12), setNumberOfElements(12), setTotalElements(20),
				setTotalPages(20 / 12 + 1), setPageNumber(0)),
			Arguments.of(RequestStatus.COMPLETE, null, setPageSize(12), setNumberOfElements(20 % 12),
				setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(20 / 12)),
			Arguments.of(null, "test1", setPageSize(12), setNumberOfElements(10), setTotalElements(10),
				setTotalPages(1), setPageNumber(0)),
			Arguments.of(null, "test2", setPageSize(12), setNumberOfElements(12), setTotalElements(40),
				setTotalPages(40 / 12 + 1), setPageNumber(0)),
			Arguments.of(RequestStatus.BEFORE, "test2", setPageSize(12), setNumberOfElements(12), setTotalElements(20),
				setTotalPages(20 / 12 + 1), setPageNumber(0)),
			Arguments.of(RequestStatus.COMPLETE, "test2", setPageSize(12), setNumberOfElements(12),
				setTotalElements(20), setTotalPages(20 / 12 + 1), setPageNumber(0))
		);
	}

	private static int setPageSize(final int pageSize) {
		return pageSize;
	}

	private static int setNumberOfElements(final int numberOfElements) {
		return numberOfElements;
	}

	private static int setTotalPages(final int totalPages) {
		return totalPages;
	}

	private static int setTotalElements(final int totalElements) {
		return totalElements;
	}

	private static int setPageNumber(final int pageNumber) {
		return pageNumber;
	}
}
