package com.example.servicehub.repository.querydsl;

import com.example.servicehub.domain.*;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.PopularityServiceDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.example.servicehub.domain.QClient.client;
import static com.example.servicehub.domain.QClientService.clientService;
import static com.example.servicehub.domain.QServiceCategory.serviceCategory;
import static com.example.servicehub.domain.QServices.services;

/**
 * select count(*) , s.service_name,s.service_URL
 * from client_service cs
 * join services s on s.service_id = cs.service_id
 * join service_category sc on sc.service_id = s.service_id
 * where s.service_name like '%잡%'  and sc.category_id in(5)
 * group by cs.service_id
 * order by count(*) desc
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceSearchRepositoryImpl implements ServiceSearchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PopularityServiceDto> search(List<String> categories, String serviceName,Pageable pageable) {
        List<PopularityServiceDto> result = queryFactory
                .select(Projections.constructor(
                        PopularityServiceDto.class,
                        clientService.count(),
                        services.serviceName,
                        services.logoStoreName,
                        services.serviceUrl,
                        services.title,
                        services.id
                ))
                .from(services)
                .leftJoin(services.clientServices,clientService)
                .where(services.in(
                        JPAExpressions
                                .selectDistinct(services)
                                .from(serviceCategory)
                                .join(serviceCategory.services,services)
                                .where(categoriesSearch(categories),nameSearch(serviceName))))
                .groupBy(services.id)
                .orderBy(clientService.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result,pageable,
                computeTotalCountQuery(()-> queryFactory
                    .selectDistinct(services)
                    .from(serviceCategory)
                    .join(serviceCategory.services,services)
                    .where(categoriesSearch(categories),nameSearch(serviceName))
                    .fetch().size()
                ));
    }

    @Override
    public Page<ClickServiceDto> searchByClient(Long clientId, List<String> categories, String serviceName, Pageable pageable){
        List<ClickServiceDto> result = queryFactory
                .selectDistinct(Projections.constructor(
                        ClickServiceDto.class,
                        clientService.clickCount,
                        services.id,
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

        return new PageImpl<>(result,pageable,
                computeTotalCountQuery(()-> queryFactory
                        .selectDistinct(clientService)
                        .from(clientService)
                        .join(clientService.client,client)
                        .join(clientService.services,services)
                        .join(services.serviceCategories,serviceCategory)
                        .where(client.id.eq(clientId), categoriesSearch(categories), nameSearch(serviceName))
                        .fetch().size()
        ));
    }

    private int computeTotalCountQuery(Supplier<Integer> supplier){
        return supplier.get();
    }


    private BooleanExpression nameSearch(String name) {
        if(name == null) return null;
        return services.serviceName.contains(name);
    }

    private BooleanExpression categoriesSearch(List<String> categories){
        if(categories == null || categories.isEmpty()) return null;
        return serviceCategory.category.name.in(categories);
    }

}
