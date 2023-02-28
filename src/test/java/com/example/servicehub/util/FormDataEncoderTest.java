package com.example.servicehub.util;

import com.example.servicehub.web.dto.SignupForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
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

    @Test
    @DisplayName("FormDataEncoder - container Collection Test")
    public void existCollection() throws Exception{
        // given

        String username = "test";
        List<String> categories = List.of("IT","BLOG");

        Pair pair = new Pair(username, categories);


        FormDataEncoder formDataEncoder = new FormDataEncoder();
        String encode = formDataEncoder.encode(pair,Pair.class);

        assertThat(encode).isEqualTo(
                "username=" + username +
                        "&categories=" + "IT" +
                        "&categories=" + "BLOG"
        );

    }


    @Test
    @DisplayName("컬렉션 필드 네임 가져오기")
    public void getCollectionFieldNames() throws Exception{

        List<String> collectionFieldNames = Arrays.stream(Pair.class.getDeclaredFields())
                .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .collect(toList());

        assertThat(collectionFieldNames)
                .contains("categories");
    }

    @Test
    @DisplayName("리플렉션 타입 검사")
    public void reflection() throws Exception{
        List<String> container = new ArrayList<>();

        assertThat(Collection.class.isAssignableFrom(container.getClass()))
                .isTrue();
    }


    static class Pair{
        String username;
        List<String> categories;

        public Pair(String username, List<String> categories) {
            this.username = username;
            this.categories = categories;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }
    }

}
