package com.example.servicehub.dto;

import com.example.servicehub.domain.Client;
import com.example.servicehub.web.validator.annotation.FileSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEditForm {

    @FileSize(maxSizeInMB = 3,message = "프로필 이미지 사이즈는 3MB이하로 업로드 해주세요")
    private MultipartFile profileImage;
    @NotBlank
    @Length(min = 1 , max = 5,message = "넥네임 길이는 1이상 5이하여야 합니다.")
    private String nickname;
    @Length(max = 1000,message = "소개말은 1000글자 미만으로 작성해주세요")
    private String introComment;
    @URL(message = "URL을 입력해야합니다")
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
