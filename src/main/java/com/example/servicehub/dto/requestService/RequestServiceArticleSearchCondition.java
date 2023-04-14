package com.example.servicehub.dto.requestService;

import com.example.servicehub.domain.requestService.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestServiceArticleSearchCondition {
    private RequestStatus requestStatus;
    private String nickname;
}
