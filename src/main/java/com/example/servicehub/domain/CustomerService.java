package com.example.servicehub.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(exclude = {"services", "clientId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_service_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services services;

    @Column(name = "client_id")
    private Long clientId;

    private long clickCount;

    public CustomerService(Long clientId, Services services) {
        this.clientId = clientId;
        this.services = services;
        this.clickCount = 0L;
    }

    public static CustomerService of(Long clientId, Services services) {
        CustomerService customerService = new CustomerService(clientId, services);
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
        private Long clientId;
        private Services services;

        CustomerServiceBuilder() {
        }

        public CustomerServiceBuilder clientId(Long clientId) {
            this.clientId = clientId;
            return this;
        }

        public CustomerServiceBuilder services(Services services) {
            this.services = services;
            return this;
        }

        public CustomerService build() {
            CustomerService customerService = new CustomerService(this.clientId, this.services);
            this.services.mappingAssociations(customerService);
            return new CustomerService(this.clientId, this.services);
        }

        public String toString() {
            return "CustomerService.CustomerServiceBuilder(clientId=" + this.clientId + ", services=" + this.services + ")";
        }
    }
}
