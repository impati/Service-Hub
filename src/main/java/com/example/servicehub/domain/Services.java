package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Services)) return false;
        Services services = (Services) o;
        return Objects.equals(id, services.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
