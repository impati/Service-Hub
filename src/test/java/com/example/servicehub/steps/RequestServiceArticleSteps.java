package com.example.servicehub.steps;

import java.util.Random;

import com.example.servicehub.domain.requestservice.RequestServiceArticle;
import com.example.servicehub.repository.requestService.RequestServiceArticleRepository;

public class RequestServiceArticleSteps {
	public static String DEFAULT_ARTICLE_TITLE = "테스트 서비스 등록 요청합니다.";
	public static String DEFAULT_ARTICLE_DESCRIPTION = "테스트 서비스 등록 요청합니다.";
	public static String DEFAULT_SERVICE_URL = "https://test.com";
	public static String DEFAULT_LOGO_STORE_NAME = "default.png";
	public static String DEFAULT_SERVICE_TITLE = "테스트 서비스 타이틀입니다.";
	public static String DEFAULT_SERVICE_CONTENT = "테스트 서비스 내용입니다.";
	public static String DEFAULT_SERVICE_NAME = "테스트 서비스";
	public static String DEFAULT_NICKNAME = "impati";
	public static long DEFAULT_CUSTOMER_ID = 1L;

	private final RequestServiceArticleRepository repository;

	public RequestServiceArticleSteps(RequestServiceArticleRepository repository) {
		this.repository = repository;
	}

	public RequestServiceArticle create() {
		return repository.save(RequestServiceArticle
			.builder()
			.articleTitle(DEFAULT_ARTICLE_TITLE)
			.articleDescription(DEFAULT_ARTICLE_DESCRIPTION)
			.serviceUrl(DEFAULT_SERVICE_URL)
			.logoStoreName(DEFAULT_LOGO_STORE_NAME)
			.serviceTitle(DEFAULT_SERVICE_TITLE)
			.serviceName(DEFAULT_SERVICE_NAME)
			.serviceContent(DEFAULT_SERVICE_CONTENT)
			.nickname(DEFAULT_NICKNAME)
			.customerId(DEFAULT_CUSTOMER_ID)
			.build());
	}

	public RequestServiceArticle createRandom(String nickname, Long customerId) {
		int random = new Random().nextInt();

		return repository.save(RequestServiceArticle
			.builder()
			.articleTitle(DEFAULT_ARTICLE_TITLE + random)
			.articleDescription(DEFAULT_ARTICLE_DESCRIPTION + random)
			.serviceUrl(DEFAULT_SERVICE_URL + random)
			.logoStoreName(DEFAULT_LOGO_STORE_NAME + random)
			.serviceTitle(DEFAULT_SERVICE_TITLE + random)
			.serviceName(DEFAULT_SERVICE_NAME)
			.serviceContent(DEFAULT_SERVICE_CONTENT + random)
			.nickname(nickname)
			.customerId(customerId)
			.build());
	}

	public RequestServiceArticle createRandomAndMerge(String nickname, Long customerId) {
		int random = new Random().nextInt();

		RequestServiceArticle article = repository.save(RequestServiceArticle
			.builder()
			.articleTitle(DEFAULT_ARTICLE_TITLE + random)
			.articleDescription(DEFAULT_ARTICLE_DESCRIPTION + random)
			.serviceUrl(DEFAULT_SERVICE_URL + random)
			.logoStoreName(DEFAULT_LOGO_STORE_NAME + random)
			.serviceTitle(DEFAULT_SERVICE_TITLE + random)
			.serviceName(DEFAULT_SERVICE_NAME)
			.serviceContent(DEFAULT_SERVICE_CONTENT + random)
			.nickname(nickname)
			.customerId(customerId)
			.build());

		article.merge();
		return article;
	}

}
