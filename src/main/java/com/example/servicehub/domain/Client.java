package com.example.servicehub.domain;

import com.example.servicehub.domain.constant.CustomRole;
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


    @Column(unique = true , nullable = false)
    private String nickname;

    @Column(unique = true , nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true , nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private CustomRole roles;

    public static Client of(String nickname, String username, String password, String email, CustomRole roles){
        return new Client(nickname,username,password,email,roles);
    }

    private Client(String nickname, String username, String password, String email, CustomRole roles) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.createdBy = username;
        this.modifiedBy = username;
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
