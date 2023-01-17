package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    private String title;

    @OneToMany(mappedBy = "services",fetch = FetchType.LAZY)
    private List<ServiceCategory> serviceCategories = new ArrayList<>();


    public void mappingAssociations(ServiceCategory serviceCategory){
        serviceCategories.add(serviceCategory);
    }

    public static Services of(String serviceName,String logoStoreName,String serviceUrl , String title, String content){
        return new Services(serviceName,logoStoreName,serviceUrl,title,content);
    }

    private Services(String serviceName,String logoStoreName, String serviceUrl, String title, String content) {
        this.title = title;
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
