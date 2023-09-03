package com.example.servicehub.web.dto.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleCustomerDto {

	private String nickname;
	private Long customerId;
	private String blogUrl;
	private String profileUrl;
	private String introComment;

	@Builder
	public SimpleCustomerDto(
		final String nickname,
		final Long customerId,
		final String blogUrl,
		final String profileUrl,
		final String introComment
	) {
		this.nickname = nickname;
		this.customerId = customerId;
		this.blogUrl = blogUrl;
		this.profileUrl = profileUrl;
		this.introComment = introComment;
	}
}
