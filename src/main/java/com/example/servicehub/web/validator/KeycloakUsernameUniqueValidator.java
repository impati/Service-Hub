package com.example.servicehub.web.validator;

import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.web.validator.annotation.KeycloakUnique;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class KeycloakUsernameUniqueValidator implements ConstraintValidator<KeycloakUnique,String> {

    private final ClientRepository clientRepository;
    private UniqueType uniqueType;

    @Override
    public void initialize(KeycloakUnique constraintAnnotation) {
        uniqueType = constraintAnnotation.uniqueType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        switch (uniqueType){
            case USERNAME: return !clientRepository.existsClientByUsername(value);
            case EMAIL: return !clientRepository.existsClientByEmail(value);
        }
        throw new IllegalStateException("유일해야하는 키 값이 잘못들어왔습니다");
    }

}
