package com.example.servicehub.domain.requestservice;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.servicehub.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "request_service_article")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestServiceArticle extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_service_article_id")
	private Long id;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Column(name = "article_title", nullable = false)
	private String articleTitle;

	@Column(name = "article_description", nullable = false, length = 100000)
	private String articleDescription;

	@Column(name = "service_url", nullable = false)
	private String serviceUrl;

	@Column(name = "logo_store_name", nullable = false)
	private String logoStoreName;

	@Column(name = "service_title", nullable = false)
	private String serviceTitle;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(name = "service_content", nullable = false, length = 10000)
	private String serviceContent;

	@Column(name = "request_status")
	@Enumerated(EnumType.STRING)
	private RequestStatus requestStatus;

	@Builder
	public RequestServiceArticle(
		final String nickname,
		final Long customerId,
		final String articleTitle,
		final String articleDescription,
		final String serviceUrl,
		final String logoStoreName,
		final String serviceTitle,
		final String serviceName,
		final String serviceContent
	) {
		this.nickname = nickname;
		this.customerId = customerId;
		this.articleTitle = articleTitle;
		this.articleDescription = articleDescription;
		this.serviceUrl = serviceUrl;
		this.logoStoreName = logoStoreName;
		this.serviceTitle = serviceTitle;
		this.serviceName = serviceName;
		this.serviceContent = serviceContent;
		requestStatus = RequestStatus.BEFORE;
	}

	public void merge() {
		requestStatus = RequestStatus.COMPLETE;
	}

	public void reject() {
		requestStatus = RequestStatus.FAIL;
	}

	public void defer() {
		requestStatus = RequestStatus.DEFER;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RequestServiceArticle)) {
			return false;
		}

		RequestServiceArticle that = (RequestServiceArticle)o;
		return this.getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
