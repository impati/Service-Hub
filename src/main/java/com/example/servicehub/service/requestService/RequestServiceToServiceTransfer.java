package com.example.servicehub.service.requestService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.domain.requestservice.RequestStatus;
import com.example.servicehub.service.services.ServicesRegister;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceToServiceTransfer {

	private final ServicesRegister servicesRegister;
	private final RequestServiceArticleSearch requestServiceArticleSearch;

	public void registerRequestedServiceAsService(
		final Long requestServiceId,
		final List<String> categories,
		final RequestStatus requestStatus
	) {
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

	boolean isRegister(final RequestStatus requestStatus) {
		return requestStatus == RequestStatus.COMPLETE;
	}

	private void setRequestStatus(final RequestServiceArticle article, final RequestStatus status) {
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
