package com.example.servicehub.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.servicehub.support.crawl.JsoupMetaDataCrawler;
import com.example.servicehub.support.crawl.MetaDataCrawler;
import com.example.servicehub.support.crawl.ServiceMetaData;

class MetaDataCrawlerTest {

    @Test
    @DisplayName("주어진 서비스 메터 정보를 잘 가져오는지 테스트")
    void getMetaData() {
        // given
        final MetaDataCrawler metaDataCrawler = new JsoupMetaDataCrawler();

        // when
        final ServiceMetaData serviceMetaData = metaDataCrawler.tryToGetMetaData("https://papago.naver.com/");

        // then
        assertThat(serviceMetaData.getSiteName()).isEqualTo("Naver papago");
    }
}
