package com.example.servicehub.dto;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.constant.CustomRole;
import com.example.servicehub.exception.PasswordNotMatchException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;

@Data
@ToString
@AllArgsConstructor
public class ClientRegisterForm {

    @Email
    private String email;

    private String nickname;

    private String username;

    private String password;

    private String repeatPassword;

    private String role;

    public void setEncode(String encodedPassword){
        if(isNotSamePassword()) throw new PasswordNotMatchException();
        this.password = encodedPassword;
    }

    public Client toEntity(){
        defaultNickname();
        return Client.of(nickname,username,password,email,CustomRole.valueOf("ROLE_USER"));
    }

    private void defaultNickname(){
        this.nickname = username;
    }

    private boolean isNotSamePassword(){
        if(!password.equals(repeatPassword)) return true;
        return false;
    }
}
