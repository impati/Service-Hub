package com.example.servicehub.dto.requestService;

import com.example.servicehub.domain.requestservice.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestServiceArticleSearchCondition {

	private RequestStatus requestStatus;
	private String nickname;
}
