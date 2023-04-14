package com.example.servicehub.dto.requestService;

import com.example.servicehub.dto.common.MultipartFileForm;
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
