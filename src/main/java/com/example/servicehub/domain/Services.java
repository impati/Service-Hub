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

    @Column(nullable = false,unique = true)
    private String serviceName;

    @Column(unique = true)
    private String logoStoreName;

    @Column(nullable = false,unique = true)
    private String serviceUrl;

    @Column(nullable = false,length = 10000)
    private String content;

    public static Services of(String serviceName,String logoStoreName,String serviceUrl , String content){
        return new Services(serviceName,logoStoreName,serviceUrl,content);
    }

    private Services(String serviceName,String logoStoreName, String serviceUrl, String content) {
        this.serviceName = serviceName;
        this.logoStoreName = logoStoreName;
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
