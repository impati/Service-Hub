package com.example.servicehub.domain.customer;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.servicehub.domain.common.BaseEntity;
import com.example.servicehub.domain.services.Services;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "customer_service")
@Entity
@Getter
@ToString(exclude = {"services", "customerId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerService extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_service_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private Services services;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "click_count")
	private long clickCount;

	@Builder
	public CustomerService(final Long customerId, final Services services) {
		this.customerId = customerId;
		this.services = services;
		this.clickCount = 0L;
	}

	public static CustomerService of(final Long customerId, final Services services) {
		final CustomerService customerService = new CustomerService(customerId, services);
		services.mappingAssociations(customerService);
		return customerService;
	}

	public synchronized void click() {
		this.clickCount += 1L;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o){
			return true;
		}
		if (!(o instanceof CustomerService)){
			return false;
		}

		CustomerService that = (CustomerService)o;
		return this.getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
