package com.example.servicehub.repository.querydsl;

import com.example.servicehub.domain.*;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.PopularityServiceDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.OrderComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.servicehub.domain.QClient.client;
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
    public Page<ClickServiceDto> searchByClient(Long clientId, List<Category> categories, String serviceName, Pageable pageable){
        List<ClickServiceDto> result = queryFactory
                .selectDistinct(Projections.constructor(
                        ClickServiceDto.class,
                        clientService.clickCount,
                        services.serviceName,
                        services.logoStoreName,
                        services.serviceUrl,
                        services.title
                ))
                .from(clientService)
                .join(clientService.client,client)
                .join(clientService.services,services)
                .join(services.serviceCategories,serviceCategory)
                .where(client.id.eq(clientId), categoriesSearch(categories), nameSearch(serviceName))
                .orderBy(clientService.clickCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(result,pageable,result.size());
    }

    @Override
    public Page<PopularityServiceDto> findServices(List<Services> serviceList, Pageable pageable) {
        List<PopularityServiceDto> result = queryFactory
                .select(Projections.constructor(
                        PopularityServiceDto.class,
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
