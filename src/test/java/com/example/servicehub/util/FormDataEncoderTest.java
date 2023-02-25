package com.example.servicehub.util;

import com.example.servicehub.web.dto.SignupForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormDataEncoderTest {


    @Test
    @DisplayName("FormDataEncoder - Test")
    public void given_when_then() throws Exception{
        // given

        String username = "test";
        String email = "test@test.com";
        String password = "password123!23A";
        String repeatPassword = "password123!23A";
        SignupForm signupForm = new SignupForm(username,email,password,repeatPassword);

        // when


        FormDataEncoder formDataEncoder = new FormDataEncoder();
        String encode = formDataEncoder.encode(signupForm,SignupForm.class);
        // then

        assertThat(encode).isEqualTo(
                "password=" + password +
                "&repeatPassword=" + repeatPassword +
                "&email=" + email +
                "&username=" + username
        );
    }
}
