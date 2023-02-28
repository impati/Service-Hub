package com.example.servicehub.support;

import com.example.servicehub.exception.ServiceConnectFailException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupMetaDataCrawler implements MetaDataCrawler{

    public ServiceMetaData tryToGetMetaData(String serviceUrl){
        if(serviceUrl == null) return new ServiceMetaData();
        try{
            return getMetaData(serviceUrl);
        }catch (Exception e){
            throw new ServiceConnectFailException(e.getMessage() + serviceUrl + " 연결에 실패하였습니다");
        }
    }

    private ServiceMetaData getMetaData(String serviceUrl) throws IOException {
        Elements elements = Jsoup
                .connect(serviceUrl)
                .get()
                .getElementsByTag("meta");
        ServiceMetaData serviceMetaData = new ServiceMetaData();
        for (var element : elements) {
            serviceMetaData.setAttributeByProperty(element.attr("property"),element.attr("content"));
            serviceMetaData.setAttributeByName(element.attr("name"),element.attr("content"));
        }
        return serviceMetaData ;
    }

}
