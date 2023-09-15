package com.example.servicehub.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.servicehub.domain.common.BaseEntity;
import com.example.servicehub.domain.customer.CustomerService;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "services")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Services extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Long id;

	@Column(name = "service_name", nullable = false, unique = true)
	private String serviceName;

	@Column(name = "logo_store_name", unique = true)
	private String logoStoreName;

	@Column(name = "service_url", nullable = false, unique = true)
	private String serviceUrl;

	@Column(name = "content", nullable = false, length = 10000)
	private String content;

	@Column(name = "title")
	private String title;

	@OneToMany(mappedBy = "services", fetch = FetchType.LAZY)
	private final List<ServiceCategory> serviceCategories = new ArrayList<>();

	@OneToMany(mappedBy = "services", fetch = FetchType.LAZY)
	private final List<CustomerService> customerServices = new ArrayList<>();

	@Builder
	public Services(
		final String serviceName,
		final String logoStoreName,
		final String serviceUrl,
		final String title,
		final String content
	) {
		this.title = title;
		this.serviceName = serviceName;
		this.logoStoreName = logoStoreName;
		this.serviceUrl = serviceUrl;
		this.content = content;
	}

	public static Services of(
		final String serviceName,
		final String logoStoreName,
		final String serviceUrl,
		final String title,
		final String content
	) {
		return new Services(serviceName, logoStoreName, serviceUrl, title, content);
	}

	public void update(
		final String serviceName,
		final String logo,
		final String serviceUrl,
		final String title,
		final String content
	) {
		this.serviceName = serviceName;
		this.logoStoreName = logo;
		this.serviceUrl = serviceUrl;
		this.title = title;
		this.content = content;
	}

	public void mappingAssociations(final ServiceCategory serviceCategory) {
		serviceCategories.add(serviceCategory);
	}

	public void mappingAssociations(final CustomerService customerService) {
		customerServices.add(customerService);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Services)) {
			return false;
		}

		Services services = (Services)o;
		return this.getId() != null && Objects.equals(getId(), services.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
