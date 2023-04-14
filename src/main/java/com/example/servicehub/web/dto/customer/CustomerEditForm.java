package com.example.servicehub.web.dto.customer;

import com.example.servicehub.security.authentication.CustomerPrincipal;
import com.example.servicehub.web.validator.annotation.FileSize;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerEditForm {
    @FileSize(maxSizeInMB = 3, message = "프로필 이미지 사이즈는 3MB이하로 업로드 해주세요.")
    private MultipartFile profileImage;

    @NotBlank
    @Length(min = 1, max = 20, message = "넥네임 길이는 1이상 20이하여야 합니다.")
    private String nickname;

    @Length(max = 1000, message = "소개말은 1000글자 미만으로 작성해주세요.")
    private String introComment;

    @URL(message = "URL을 입력해야합니다.")
    private String blogUrl;

    public CustomerEditForm(String nickname, String introComment, String blogUrl) {
        this.nickname = nickname;
        this.introComment = introComment;
        this.blogUrl = blogUrl;
    }

    public static CustomerEditForm from(CustomerPrincipal customerPrincipal) {
        return new CustomerEditForm(
                customerPrincipal.getNickname(),
                customerPrincipal.getIntroduceComment(),
                customerPrincipal.getBlogUrl());
    }
}
