package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CustomServiceForm {

    @NotBlank
    private String serviceName;

    @NotBlank
    @URL
    private String serviceUrl;

}
