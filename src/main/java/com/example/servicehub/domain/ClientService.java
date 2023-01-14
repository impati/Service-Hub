package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(exclude ={"services" , "client"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientService extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_service_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public static ClientService of(Client client , Services services){
        return new ClientService(client,services);
    }

    private ClientService(Client client , Services services){
        this.client = client;
        this.services =  services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientService)) return false;
        ClientService that = (ClientService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
