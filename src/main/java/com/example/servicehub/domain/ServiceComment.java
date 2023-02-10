package com.example.servicehub.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(exclude = {"services","client"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_comment_id")
    private Long id;

    @Column(nullable = false,length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="service_id")
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="client_id")
    private Client client;

    public void updateContent(String content){
        this.content = content;
    }

    public static ServiceComment of(String content,Services services , Client client){
        return new ServiceComment(content,services,client);
    }

    private ServiceComment (String content,Services services , Client client){
        this.content = content;
        this.services = services;
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceComment)) return false;
        ServiceComment that = (ServiceComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
