package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(exclude = {"services", "clientId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientService extends BaseEntity {
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

    @Builder
    public ClientService(Long clientId, Services services) {
        this.clientId = clientId;
        this.services = services;
        this.clickCount = 0L;
    }

    public static ClientService of(Long clientId, Services services) {
        return new ClientService(clientId, services);
    }

    public void click() {
        this.clickCount += 1L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientService)) return false;
        ClientService that = (ClientService) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
