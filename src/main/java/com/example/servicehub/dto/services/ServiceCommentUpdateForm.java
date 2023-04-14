package com.example.servicehub.dto.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCommentUpdateForm {

    private Long commentId;

    private Long customerId;

    @Length(min = 1, max = 2000, message = "댓글은 1글자 이상 2000글자 이내로 작성해주세요.")
    private String content;

    private Long serviceId;

    public ServiceCommentUpdateForm(Long commentId, Long customerId, String content) {
        this.commentId = commentId;
        this.customerId = customerId;
        this.content = content;
    }

    public void assignCustomer(Long customerId) {
        this.customerId = customerId;
    }
}
