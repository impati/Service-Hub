package com.example.servicehub.repository.querydsl;

import com.example.servicehub.domain.RequestServiceArticle;
import com.example.servicehub.domain.RequestStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.servicehub.domain.QRequestServiceArticle.requestServiceArticle;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class RequestServiceArticleFinder {
    private final JPAQueryFactory queryFactory;

    public Page<RequestServiceArticle> findArticle(RequestStatus requestStatus, String nickname, Pageable pageable) {

        List<RequestServiceArticle> result = queryFactory
                .selectFrom(requestServiceArticle)
                .where(requestStatus(requestStatus), nickname(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long totalCount = queryFactory
                .selectFrom(requestServiceArticle)
                .where(requestStatus(requestStatus), nickname(nickname))
                .fetch().size();

        return new PageImpl<>(result, pageable, totalCount);
    }

    private BooleanExpression requestStatus(RequestStatus requestStatus) {
        if (requestStatus == null) return null;
        return requestServiceArticle.requestStatus.eq(requestStatus);
    }

    private BooleanExpression nickname(String nickname) {
        if (nickname == null) return null;
        return requestServiceArticle.nickname.containsIgnoreCase(nickname);
    }

}
