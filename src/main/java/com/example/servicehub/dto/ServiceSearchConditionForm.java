package com.example.servicehub.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceSearchConditionForm {
    List<String> categories;
    String serviceName;

    public static ServiceSearchConditionForm of(List<String> categories, String serviceName){
        return new ServiceSearchConditionForm(categories,serviceName);
    }

    private ServiceSearchConditionForm(List<String> categories, String serviceName) {
        this.categories = categories;
        this.serviceName = serviceName;
    }
}
