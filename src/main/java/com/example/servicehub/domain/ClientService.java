package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude ={"services" , "client"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client_service extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_service_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @Column(nullable = false)
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @Column(nullable = false)
    private Client client;

    public static Client_service of(Client client , Services services){
        return new Client_service(client,services);
    }

    private Client_service(Client client , Services services){
        this.client = client;
        this.services =  services;
    }

}
