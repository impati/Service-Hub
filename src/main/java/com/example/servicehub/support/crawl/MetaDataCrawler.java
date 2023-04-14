package com.example.servicehub.support.crawl;

public interface MetaDataCrawler {
    ServiceMetaData tryToGetMetaData(String serviceUrl);
}
