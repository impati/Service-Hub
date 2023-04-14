package com.example.servicehub.domain.customer;

import com.example.servicehub.domain.services.Services;
import com.example.servicehub.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

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

    private long clickCount;

    public CustomerService(Long customerId, Services services) {
        this.customerId = customerId;
        this.services = services;
        this.clickCount = 0L;
    }

    public static CustomerService of(Long customerId, Services services) {
        CustomerService customerService = new CustomerService(customerId, services);
        services.mappingAssociations(customerService);
        return customerService;
    }

    public static CustomerServiceBuilder builder() {
        return new CustomerServiceBuilder();
    }

    public void click() {
        this.clickCount += 1L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerService)) return false;
        CustomerService that = (CustomerService) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class CustomerServiceBuilder {
        private Long customerId;
        private Services services;

        CustomerServiceBuilder() {
        }

        public CustomerServiceBuilder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerServiceBuilder services(Services services) {
            this.services = services;
            return this;
        }

        public CustomerService build() {
            CustomerService customerService = new CustomerService(this.customerId, this.services);
            this.services.mappingAssociations(customerService);
            return new CustomerService(this.customerId, this.services);
        }

        public String toString() {
            return "CustomerService.CustomerServiceBuilder(customerId=" + this.customerId + ", services=" + this.services + ")";
        }
    }
}
