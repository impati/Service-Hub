package com.example.servicehub.dto;

import com.example.servicehub.domain.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleClientDto {

    private String nickname;
    private Long clientId;
    private String blogUrl;
    private String profileUrl;
    private String introComment;
    public static SimpleClientDto from(Client client){
        return new SimpleClientDto(client.getNickname(),client.getId(),client.getBlogUrl(),client.getProfileImageUrl(),client.getIntroduceComment());
    }


}
