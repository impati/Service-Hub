package com.example.servicehub.domain.custom;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.servicehub.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "custom_service")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomService extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "custom_service_id")
	private Long id;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(name = "logo_store_name", nullable = false, unique = true)
	private String logoStoreName;

	@Column(name = "service_url", nullable = false)
	private String serviceUrl;

	@Column(name = "title")
	private String title;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "click_count")
	private long clickCount;

	@Builder
	public CustomService(
		final String serviceName,
		final String logoStoreName,
		final String serviceUrl,
		final String title, Long customerId
	) {
		this.serviceName = serviceName;
		this.logoStoreName = logoStoreName;
		this.serviceUrl = serviceUrl;
		this.title = title;
		this.customerId = customerId;
	}

	public synchronized void click() {
		this.clickCount += 1L;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o){
			return true;
		}
		if (!(o instanceof CustomService)){
			return false;
		}
		CustomService that = (CustomService)o;
		return this.getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

