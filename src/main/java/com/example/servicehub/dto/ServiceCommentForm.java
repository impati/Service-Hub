package com.example.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCommentForm {

    @NotNull
    private Long serviceId;

    private Long clientId;

    @Length(min = 1 , max = 2000,message = "댓글은 1글자 이상 2000글자 이내로 작성해주세요.")
    private String content;

    public void assignAuthor(Long clientId){
        this.clientId = clientId;
    }
}
