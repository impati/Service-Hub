package com.example.servicehub.web.validator.annotation;

import com.example.servicehub.web.validator.KeycloakUsernameUniqueValidator;
import com.example.servicehub.web.validator.UniqueType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD
        ,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KeycloakUsernameUniqueValidator.class)
@Documented
public @interface KeycloakUnique {
    Class<? extends Payload> [] payload() default{};
    Class<?>[] groups() default {};
    UniqueType uniqueType() default UniqueType.USERNAME;
    String message() default "이미 등록된 아이디입니다.";
}
