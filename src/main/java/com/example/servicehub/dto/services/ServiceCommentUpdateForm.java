package com.example.servicehub.dto.services;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCommentUpdateForm {

	@Length(min = 1, max = 2000, message = "댓글은 1글자 이상 2000글자 이내로 작성해주세요.")
	private String content;

	private Long commentId;
	private Long customerId;
	private Long serviceId;

	public ServiceCommentUpdateForm(
		final Long commentId,
		final Long customerId,
		final String content
	) {
		this.commentId = commentId;
		this.customerId = customerId;
		this.content = content;
	}

	public void assignCustomer(final Long customerId) {
		this.customerId = customerId;
	}
}
