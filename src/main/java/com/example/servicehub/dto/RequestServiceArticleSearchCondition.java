package com.example.servicehub.dto;

import com.example.servicehub.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestServiceArticleSearchCondition {
    private RequestStatus requestStatus;
    private String nickname;
}
