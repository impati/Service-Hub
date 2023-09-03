package com.example.servicehub.repository.requestService;

import static com.example.servicehub.domain.requestservice.QRequestServiceArticle.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RequestServiceArticleFinder {

	private final JPAQueryFactory queryFactory;

	public Page<RequestServiceArticle> findArticle(
		final RequestStatus requestStatus,
		final String nickname,
		final Pageable pageable
	) {
		final List<RequestServiceArticle> result = queryFactory
			.selectFrom(requestServiceArticle)
			.where(requestStatus(requestStatus), nickname(nickname))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final long totalCount = queryFactory
			.selectFrom(requestServiceArticle)
			.where(requestStatus(requestStatus), nickname(nickname))
			.fetch().size();

		return new PageImpl<>(result, pageable, totalCount);
	}

	private BooleanExpression requestStatus(final RequestStatus requestStatus) {
		if (requestStatus != null) {
			return requestServiceArticle.requestStatus.eq(requestStatus);
		}

		return null;
	}

	private BooleanExpression nickname(final String nickname) {
		if (nickname != null) {
			return requestServiceArticle.nickname.containsIgnoreCase(nickname);
		}

		return null;
	}
}
