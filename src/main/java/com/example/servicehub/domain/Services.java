package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Services extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="service_id")
    private Long id;

    @Column(unique = true)
    private String logoImageUrl;

    @Column(nullable = false,unique = true)
    private String serviceUrl;

    @Column(nullable = false,length = 10000)
    private String content;

    public static Services of(String logoImageUrl,String serviceUrl , String content){
        return new Services(logoImageUrl,serviceUrl,content);
    }

    private Services(String logoImageUrl, String serviceUrl, String content) {
        this.logoImageUrl = logoImageUrl;
        this.serviceUrl = serviceUrl;
        this.content = content;
    }
}
