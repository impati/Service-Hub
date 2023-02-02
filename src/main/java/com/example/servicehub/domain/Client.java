package com.example.servicehub.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="client_id")
    private Long id;

    @Column(name = "sub", unique = true)
    @NotNull
    private String userId;

    @Column(unique = true , nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(unique = true , nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;


    public static Client of(String userId , String nickname, String username, String email, String role,ProviderType providerType){
        return new Client(userId ,nickname,username,email,role,providerType);
    }

    private Client(String userId,String nickname, String username, String email, String role,ProviderType providerType) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.roleType = RoleType.of(role);
        this.createdBy = username;
        this.modifiedBy = username;
        this.providerType = providerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
