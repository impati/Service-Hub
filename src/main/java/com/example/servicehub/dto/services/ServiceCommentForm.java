package com.example.servicehub.dto.services;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceCommentForm {

	@NotNull
	private Long serviceId;

	@Length(min = 1, max = 2000, message = "댓글은 1글자 이상 2000글자 이내로 작성해주세요.")
	private String content;

	private Long customerId;
	private String nickname;
}
