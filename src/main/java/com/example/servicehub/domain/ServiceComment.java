package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"services","client"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ServiceComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_comment_id")
    private Long id;

    @Column(nullable = false,length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="service_id")
    @Column(nullable = false)
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="client_id")
    @Column(nullable = false)
    private Client client;


    public static ServiceComment of(String content,Services services , Client client){
        return new ServiceComment(content,services,client);
    }

    private ServiceComment (String content,Services services , Client client){
        this.content = content;
        this.services = services;
        this.client = client;
    }

}
