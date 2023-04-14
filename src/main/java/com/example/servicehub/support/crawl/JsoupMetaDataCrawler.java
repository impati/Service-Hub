package com.example.servicehub.support.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

@Slf4j
public class JsoupMetaDataCrawler implements MetaDataCrawler {

    public ServiceMetaData tryToGetMetaData(String serviceUrl) {
        if (serviceUrl == null) return new ServiceMetaData();
        try {
            return getMetaData(serviceUrl);
        } catch (Exception e) {
            log.error(e.getMessage() + serviceUrl + " 연결에 실패하였습니다");
            return new ServiceMetaData();
        }
    }

    private ServiceMetaData getMetaData(String serviceUrl) throws IOException {
        Elements elements = Jsoup
                .connect(serviceUrl)
                .timeout(3000)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .get()
                .getElementsByTag("meta");
        ServiceMetaData serviceMetaData = new ServiceMetaData(serviceUrl);
        for (var element : elements) {
            serviceMetaData.setAttributeByProperty(element.attr("property"), element.attr("content"));
            serviceMetaData.setAttributeByName(element.attr("name"), element.attr("content"));
        }

        return serviceMetaData;
    }

}
