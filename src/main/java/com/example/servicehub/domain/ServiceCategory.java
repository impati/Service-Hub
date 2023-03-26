package com.example.servicehub.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

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

    private ServiceCategory(Services services, Category category) {
        this.services = services;
        this.category = category;
    }

    public static ServiceCategory of(Services services, Category category) {
        ServiceCategory serviceCategory = new ServiceCategory(services, category);
        services.mappingAssociations(serviceCategory);
        return serviceCategory;
    }

    public static ServiceCategoryBuilder builder() {
        return new ServiceCategoryBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceCategory)) return false;
        ServiceCategory that = (ServiceCategory) o;
        return this.getId() != null && Objects.equals(id, that.id);
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

        public ServiceCategoryBuilder services(Services services) {
            this.services = services;
            return this;
        }

        public ServiceCategoryBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public ServiceCategory build() {
            ServiceCategory serviceCategory = new ServiceCategory(this.services, this.category);
            this.services.mappingAssociations(serviceCategory);
            return serviceCategory;
        }

        public String toString() {
            return "ServiceCategory.ServiceCategoryBuilder(services=" + this.services + ", category=" + this.category + ")";
        }
    }
}
