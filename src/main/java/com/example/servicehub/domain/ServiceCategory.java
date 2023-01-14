package com.example.servicehub.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude ={"services" , "category"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Service_Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @Column(nullable = false)
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @Column(nullable = false)
    private Category category;

    public static Service_Category of(Services services , Category category){
        return new Service_Category(services,category);
    }

    private Service_Category (Services services , Category category){
        this.services = services;
        this.category = category;
    }

}
