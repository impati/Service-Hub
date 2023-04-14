package com.example.servicehub.repository.services;

import com.example.servicehub.domain.category.QCategory;
import com.example.servicehub.dto.services.ClickServiceDto;
import com.example.servicehub.dto.services.PopularityServiceDto;
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
import java.util.function.Supplier;

import static com.example.servicehub.domain.customer.QCustomerService.customerService;
import static com.example.servicehub.domain.services.QServiceCategory.serviceCategory;
import static com.example.servicehub.domain.services.QServices.services;

@Slf4j
@RequiredArgsConstructor
public class ServiceSearchRepositoryImpl implements ServiceSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PopularityServiceDto> search(List<String> categories, String serviceName, Pageable pageable) {
        List<PopularityServiceDto> result = queryFactory
                .select(Projections.constructor(
                        PopularityServiceDto.class,
                        customerService.count(),
                        services.serviceName,
                        services.logoStoreName,
                        services.serviceUrl,
                        services.title,
                        services.id
                ))
                .from(services)
                .leftJoin(services.customerServices, customerService)
                .where(services.in(
                        JPAExpressions
                                .selectDistinct(services)
                                .from(serviceCategory)
                                .join(serviceCategory.services, services)
                                .where(categoriesSearch(categories), nameSearch(serviceName))))
                .groupBy(services.id)
                .orderBy(customerService.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable,
                computeTotalCountQuery(() -> queryFactory
                        .selectDistinct(services)
                        .from(serviceCategory)
                        .join(serviceCategory.services, services)
                        .where(categoriesSearch(categories), nameSearch(serviceName))
                        .fetch().size()
                ));
    }

    @Override
    public List<ClickServiceDto> searchByCustomer(Long customerId, List<String> categories, String serviceName) {
        return queryFactory
                .selectDistinct(Projections.constructor(
                        ClickServiceDto.class,
                        services,
                        customerService.clickCount))
                .from(customerService)
                .join(customerService.services, services)
                .join(services.serviceCategories, serviceCategory).fetchJoin()
                .join(serviceCategory.category, QCategory.category).fetchJoin()
                .where(customerService.customerId.eq(customerId), categoriesSearch(categories), nameSearch(serviceName))
                .fetch();
    }

    private int computeTotalCountQuery(Supplier<Integer> supplier) {
        return supplier.get();
    }


    private BooleanExpression nameSearch(String name) {
        if (name == null) return null;
        return services.serviceName.contains(name);
    }

    private BooleanExpression categoriesSearch(List<String> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return serviceCategory.category.name.in(categories);
    }

}
