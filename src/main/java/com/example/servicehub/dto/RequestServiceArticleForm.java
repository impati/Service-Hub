package com.example.servicehub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestServiceArticleForm extends MultipartFileForm {
    private String articleTitle;
    private String articleDescription;
    private String serviceUrl;
    private String serviceTitle;
    private String serviceName;
    private String serviceContent;
}
