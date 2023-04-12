package com.example.servicehub.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestServiceArticle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String articleTitle;

    @Column(nullable = false, length = 100000)
    private String articleDescription;

    @Column(nullable = false)
    private String serviceUrl;

    @Column(nullable = false)
    private String logoStoreName;

    @Column(nullable = false)
    private String serviceTitle;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false, length = 10000)
    private String serviceContent;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Builder
    public RequestServiceArticle(String nickname, Long customerId,
                                 String articleTitle, String articleDescription,
                                 String serviceUrl, String logoStoreName,
                                 String serviceTitle, String serviceName,
                                 String serviceContent) {
        this.nickname = nickname;
        this.customerId = customerId;
        this.articleTitle = articleTitle;
        this.articleDescription = articleDescription;
        this.serviceUrl = serviceUrl;
        this.logoStoreName = logoStoreName;
        this.serviceTitle = serviceTitle;
        this.serviceName = serviceName;
        this.serviceContent = serviceContent;
        requestStatus = RequestStatus.BEFORE;
    }

    public void merge() {
        requestStatus = RequestStatus.COMPLETE;
    }

    public void reject() {
        requestStatus = RequestStatus.FAIL;
    }

    public void defer() {
        requestStatus = RequestStatus.DEFER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestServiceArticle)) return false;
        RequestServiceArticle that = (RequestServiceArticle) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
