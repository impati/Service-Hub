package com.example.servicehub.support.crawl;

public interface MetaDataCrawler {

    ServiceMetaData tryToGetMetaData(final String serviceUrl);
}
