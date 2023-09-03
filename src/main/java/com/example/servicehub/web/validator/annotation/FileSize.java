package com.example.servicehub.web.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.example.servicehub.web.validator.FileSizeValidator;

@Target({ElementType.FIELD, ElementType.METHOD
    , ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface FileSize {

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    long maxSizeInMB() default 512;

    String message() default "파일 업로드 사이즈 제한을 지켜야합니다.";
}
