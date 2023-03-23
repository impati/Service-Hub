package com.example.servicehub.support;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class JsoupMetaDataCrawlerTest {


    JsoupMetaDataCrawler crawler = new JsoupMetaDataCrawler();

    static Stream<Arguments> crawlerTest() {
        return Stream.of(
                Arguments.of("https://github.com/", "https://github.com/", "GitHub: Let’s build from here", "@github"),
                Arguments.of("https://www.inflearn.com/", "https://www.inflearn.com/", "인프런 - 라이프타임 커리어 플랫폼", "인프런"),
                Arguments.of("https://papago.naver.com/", "https://papago.naver.com/", "Free translation service, Papago", "Naver papago")
        );
    }

    @Test
    @DisplayName("crawler 연결 테스트 test")
    public void serviceConnectTest() throws Exception {

        assertThatCode(() -> Jsoup
                .connect("https://www.udemy.com/")
                .userAgent("Mozilla/5.0")
                .get()
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("ServiceMetaData 테스트")
    @MethodSource("crawlerTest")
    public void getServiceMetaData(String serviceUrl, String expectedServiceUrl, String title, String siteName) throws Exception {

        ServiceMetaData serviceMetaData = crawler.tryToGetMetaData(serviceUrl);

        assertThat(serviceMetaData.getUrl())
                .isEqualTo(expectedServiceUrl);

        assertThat(serviceMetaData.getTitle())
                .isEqualTo(title);

        assertThat(serviceMetaData.getSiteName())
                .isEqualTo(siteName);

    }

}