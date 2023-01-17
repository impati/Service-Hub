package com.example.servicehub.repository.querydsl;

import com.example.servicehub.domain.*;
import com.example.servicehub.dto.PopularityService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.servicehub.domain.QClientService.clientService;
import static com.example.servicehub.domain.QServiceCategory.serviceCategory;
import static com.example.servicehub.domain.QServices.services;

@Slf4j
@RequiredArgsConstructor
public class ServiceSearchRepositoryImpl implements ServiceSearchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Services> search(List<Category> categories, String serviceName) {
        return queryFactory
                .selectDistinct(services)
                .from(serviceCategory)
                .join(serviceCategory.services,services)
                .where(categoriesSearch(categories),nameSearch(serviceName))
                .fetch();
    }

    @Override
    public Page<PopularityService> findServicesSortedByPopularity(List<Services> serviceList, Pageable pageable) {
        List<PopularityService> result = queryFactory
                .select(Projections.constructor(
                        PopularityService.class,
                        JPAExpressions
                                .select(clientService.count())
                                .from(clientService)
                                .where(clientService.services.eq(services)),
                        services.serviceName,
                        services.logoStoreName,
                        services.serviceUrl,
                        services.title
                ))
                .from(services)
                .where(services.in(serviceList))
                .fetch();
        return new PageImpl<>(result,pageable, result.size());
    }

    private BooleanExpression nameSearch(String name) {
        if(name == null) return null;
        return services.serviceName.contains(name);
    }

    private BooleanExpression categoriesSearch(List<Category> categories){
        if(categories == null || categories.isEmpty()) return null;
        return serviceCategory.category.in(categories);
    }

}