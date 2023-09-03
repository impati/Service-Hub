package com.example.servicehub.support;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.servicehub.support.crawl.JsoupMetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;

class JsoupMetaDataCrawlerTest {

	private final JsoupMetaDataCrawler crawler = new JsoupMetaDataCrawler();

	@ParameterizedTest
	@DisplayName("ServiceMetaData 테스트")
	@MethodSource("crawlerTest")
	void getServiceMetaData(
		final String serviceUrl,
		final String expectedServiceUrl,
		final String title,
		final String siteName
	) {
		final ServiceMetaData serviceMetaData = crawler.tryToGetMetaData(serviceUrl);

		assertThat(serviceMetaData.getUrl()).isEqualTo(expectedServiceUrl);
		assertThat(serviceMetaData.getTitle()).isEqualTo(title);
		assertThat(serviceMetaData.getSiteName()).isEqualTo(siteName);
	}

	@Test
	@DisplayName("특정 서비스 메타 정보 요청시 오류 발생 테스트")
	void WhenTryToGetServiceMetaData() {
		final List<String> serviceUrlContainer = new ArrayList<>(List.of(
			"https://programmers.co.kr/",
			"https://www.coupang.com/"
		));

		for (var serviceUrl : serviceUrlContainer) {
			assertThatCode(() -> crawler.tryToGetMetaData(serviceUrl)).doesNotThrowAnyException();
		}
	}

	private static Stream<Arguments> crawlerTest() {
		return Stream.of(
			Arguments.of("https://github.com/", "https://github.com/", "GitHub: Let’s build from here", "@github"),
			Arguments.of("https://www.inflearn.com/", "https://www.inflearn.com/", "인프런 - 라이프타임 커리어 플랫폼", "인프런"),
			Arguments.of("https://papago.naver.com/", "https://papago.naver.com/", "Free translation service, Papago",
				"Naver papago")
		);
	}
}
