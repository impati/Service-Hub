package com.example.servicehub.support;

import com.example.servicehub.support.crawl.JsoupMetaDataCrawler;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MetaDataCrawlerTest {

    @Test
    @DisplayName("주어진 서비스 메터 정보를 잘 가져오는지 테스트")
    public void given_when_then() throws Exception {

        // given
        MetaDataCrawler metaDataCrawler = new JsoupMetaDataCrawler();
        // when
        ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData("https://papago.naver.com/");

        // then
        Assertions.assertThat(serviceMetaData.getSiteName())
                .isEqualTo("Naver papago");
    }
}