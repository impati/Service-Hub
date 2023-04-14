package com.example.servicehub.service.requestService;

import com.example.servicehub.domain.requestService.RequestServiceArticle;
import com.example.servicehub.domain.requestService.RequestStatus;
import com.example.servicehub.service.services.ServicesRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceToServiceTransfer {

    private final ServicesRegister servicesRegister;
    private final RequestServiceArticleSearch requestServiceArticleSearch;

    public void registerRequestedServiceAsService(Long requestServiceId, List<String> categories, RequestStatus requestStatus) {

        RequestServiceArticle requestedService = requestServiceArticleSearch.searchSingleArticle(requestServiceId);
        if (isRegister(requestStatus)) {
            servicesRegister.registerServices(
                    categories,
                    requestedService.getServiceName(),
                    requestedService.getServiceUrl(),
                    requestedService.getServiceTitle(),
                    requestedService.getServiceContent(),
                    requestedService.getLogoStoreName());
        }
        setRequestStatus(requestedService, requestStatus);
    }

    boolean isRegister(RequestStatus requestStatus) {
        return requestStatus == RequestStatus.COMPLETE;
    }

    private void setRequestStatus(RequestServiceArticle article, RequestStatus status) {
        switch (status) {
            case COMPLETE:
                article.merge();
                break;
            case DEFER:
                article.defer();
                break;
            case FAIL:
                article.reject();
                break;
        }
    }
}
