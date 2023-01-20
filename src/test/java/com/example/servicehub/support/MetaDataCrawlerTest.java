package com.example.servicehub.support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetaDataCrawlerTest {

    @Test
    @DisplayName("주어진 서비스 메터 정보를 잘 가져오는지 테스트")
    public void given_when_then() throws Exception{

        // given
        MetaDataCrawler metaDataCrawler = new MetaDataCrawler();
        // when
        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData("https://papago.naver.com/");

        // then
        Assertions.assertThat(serviceMetaData.getSiteName())
                .isEqualTo("Naver papago");
    }
}