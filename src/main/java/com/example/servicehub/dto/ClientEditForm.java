package com.example.servicehub.dto;

import com.example.servicehub.domain.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEditForm {

    private MultipartFile profileImage;
    private String nickname;
    private String introComment;
    private String blogUri;

    public ClientEditForm(String nickname, String introComment, String blogUri) {
        this.nickname = nickname;
        this.introComment = introComment;
        this.blogUri = blogUri;
    }

    public static ClientEditForm from(Client client){
       return new ClientEditForm(client.getNickname(),client.getIntroduceComment(),
               client.getBlogUrl());
    }

}
