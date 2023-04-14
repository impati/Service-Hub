package com.example.servicehub.domain.services;

import com.example.servicehub.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString(exclude = {"services", "customerId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_comment_id")
    private Long id;

    @Column(nullable = false, length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services services;

    @Column(name = "customer_id")
    private Long customerId;

    private String nickname;

    private ServiceComment(String content, Services services, Long customerId, String nickname) {
        this.content = content;
        this.services = services;
        this.customerId = customerId;
        this.nickname = nickname;
    }

    public static ServiceComment of(String content, Services services, Long customerId, String nickname) {
        return new ServiceComment(content, services, customerId, nickname);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceComment)) return false;
        ServiceComment that = (ServiceComment) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
