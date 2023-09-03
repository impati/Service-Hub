package com.example.servicehub.dto.services;

import java.time.LocalDate;

import com.example.servicehub.domain.services.ServiceComment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServiceCommentsDto {

	private Long commentId;
	private String content;
	private Long customerId;
	private String nickname;
	private LocalDate createAt;
	private LocalDate lastUpdateAt;

	private ServiceCommentsDto(
		final Long commentId,
		final String content,
		final Long customerId,
		final String nickname,
		final LocalDate createAt,
		final LocalDate lastUpdateAt
	) {
		this.commentId = commentId;
		this.content = content;
		this.customerId = customerId;
		this.nickname = nickname;
		this.createAt = createAt;
		this.lastUpdateAt = lastUpdateAt;
	}

	public static ServiceCommentsDto of(final ServiceComment serviceComment) {
		return new ServiceCommentsDto(
			serviceComment.getId(),
			serviceComment.getContent(),
			serviceComment.getCustomerId(),
			serviceComment.getNickname(),
			serviceComment.getCreatedAt(),
			serviceComment.getUpdatedAt());
	}
}
