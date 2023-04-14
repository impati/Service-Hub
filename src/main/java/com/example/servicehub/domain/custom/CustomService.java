package com.example.servicehub.domain.custom;

import com.example.servicehub.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_service_id")
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false, unique = true)
    private String logoStoreName;

    @Column(nullable = false)
    private String serviceUrl;

    private String title;

    @Column(name = "customer_id")
    private Long customerId;

    private long clickCount;

    @Builder
    public CustomService(String serviceName, String logoStoreName, String serviceUrl, String title, Long customerId) {
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
        this.serviceUrl = serviceUrl;
        this.title = title;
        this.customerId = customerId;
    }

    public void click() {
        this.clickCount += 1L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomService)) return false;
        CustomService that = (CustomService) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

