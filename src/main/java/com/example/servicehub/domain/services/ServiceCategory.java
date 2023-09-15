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
import javax.persistence.Table;

import com.example.servicehub.domain.category.Category;
import com.example.servicehub.domain.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "service_category")
@Entity
@Getter
@ToString(exclude = {"services", "category"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_category_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private Services services;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	private ServiceCategory(final Services services, final Category category) {
		this.services = services;
		this.category = category;
	}

	public static ServiceCategory of(final Services services, final Category category) {
		final ServiceCategory serviceCategory = new ServiceCategory(services, category);
		services.mappingAssociations(serviceCategory);
		return serviceCategory;
	}

	public static ServiceCategoryBuilder builder() {
		return new ServiceCategoryBuilder();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ServiceCategory)) {
			return false;
		}

		ServiceCategory that = (ServiceCategory)o;
		return this.getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public static class ServiceCategoryBuilder {
		private Services services;
		private Category category;

		ServiceCategoryBuilder() {
		}

		public ServiceCategoryBuilder services(final Services services) {
			this.services = services;
			return this;
		}

		public ServiceCategoryBuilder category(final Category category) {
			this.category = category;
			return this;
		}

		public ServiceCategory build() {
			final ServiceCategory serviceCategory = new ServiceCategory(this.services, this.category);
			this.services.mappingAssociations(serviceCategory);
			return serviceCategory;
		}

		public String toString() {
			return "ServiceCategory.ServiceCategoryBuilder(services=" + this.services + ", category=" + this.category
				+ ")";
		}
	}
}
