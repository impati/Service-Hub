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
        if(uniqueType == UniqueType.USERNAME) {
            if (clientRepository.findByUsername(value).isPresent()) return false;
            return true;
        }
        else {
            if (clientRepository.findByEmail(value).isPresent()) return false;
            return true;
        }
    }

}
