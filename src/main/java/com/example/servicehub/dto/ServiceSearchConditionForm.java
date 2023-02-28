package com.example.servicehub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServiceSearchConditionForm {
    private List<String> categories;
    private String serviceName;

    public static ServiceSearchConditionForm of(List<String> categories, String serviceName){
        return new ServiceSearchConditionForm(categories,serviceName);
    }

    private ServiceSearchConditionForm(List<String> categories, String serviceName) {
        this.categories = categories;
        this.serviceName = serviceName;
    }
}
