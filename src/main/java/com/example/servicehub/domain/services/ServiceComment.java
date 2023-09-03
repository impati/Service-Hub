package com.example.servicehub.domain.services;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.servicehub.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"services", "customerId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_comment_id")
	private Long id;

	@Column(name = "content", nullable = false, length = 10000)
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private Services services;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "nickname")
	private String nickname;

	private ServiceComment(
		final String content,
		final Services services,
		final Long customerId,
		final String nickname
	) {
		this.content = content;
		this.services = services;
		this.customerId = customerId;
		this.nickname = nickname;
	}

	public static ServiceComment of(
		final String content,
		final Services services,
		final Long customerId,
		final String nickname
	) {
		return new ServiceComment(content, services, customerId, nickname);
	}

	public void updateContent(final String content) {
		this.content = content;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ServiceComment))
			return false;
		ServiceComment that = (ServiceComment)o;
		return this.getId() != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
