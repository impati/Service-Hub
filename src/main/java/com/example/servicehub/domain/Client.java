package com.example.servicehub.domain;

import com.example.servicehub.domain.constant.CustomRole;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 *  1.서비스 회원 가입
 *  2.서비스 회원 가입 후 SSO 연동
 *  3.SSO 로그인 후 서비스 회원 가입 (O)
 */

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Client extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="client_id")
    private Long id;


    @Column(unique = true , nullable = false)
    private String nickname;

    @Column(unique = true , nullable = false)
    private String username;

    @Column(unique = true , nullable = false)
    private String password;

    @Column(unique = true , nullable = false)
    private String email;

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
    }
}
