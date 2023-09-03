package com.example.servicehub.dto.requestService;

import com.example.servicehub.dto.common.MultipartFileForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestServiceArticleForm extends MultipartFileForm {

	private String articleTitle;
	private String articleDescription;
	private String serviceUrl;
	private String serviceTitle;
	private String serviceName;
	private String serviceContent;
}
