package com.example.servicehub.web.dto;

import com.example.servicehub.web.validator.UniqueType;
import com.example.servicehub.web.validator.annotation.KeycloakUnique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupForm {

    @KeycloakUnique(uniqueType = UniqueType.USERNAME)
    private String username;

    @Email
    @KeycloakUnique(uniqueType = UniqueType.EMAIL,message = "이미 등록되어 있는 이메일 입니다")
    private String email;


    @NotBlank
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank
    private String repeatPassword;

    public boolean isSamePassword(){
        if(password.equals(repeatPassword)) return true;
        return false;
    }
}
