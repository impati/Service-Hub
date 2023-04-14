package com.example.servicehub.security.authentication;

import com.example.servicehub.domain.customer.ProviderType;
import com.example.servicehub.domain.customer.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class CustomerPrincipal implements Principal, UserDetails {

    private Long id;
    private String username;
    private String email;
    private ProviderType providerType;
    private RoleType roleType;
    private Collection<GrantedAuthority> authorities;
    private String blogUrl;
    private String profileImageUrl;
    private String introduceComment;
    private String nickname;

    @Builder
    private CustomerPrincipal(Long id, String username, String nickname,
                              String email, ProviderType providerType, RoleType roleType,
                              Collection<GrantedAuthority> authorities, String blogUrl, String profileImageUrl,
                              String introduceComment) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.providerType = providerType;
        this.roleType = roleType;
        this.authorities = authorities;
        this.blogUrl = blogUrl;
        this.profileImageUrl = profileImageUrl;
        this.introduceComment = introduceComment;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return nickname;
    }

    public void editNickname(String nickname) {
        this.nickname = nickname;
    }

    public void update(String nickname, String blogUrl, String introduceComment, String profileImageUrl) {
        if (StringUtils.hasText(nickname)) this.nickname = nickname;
        if (StringUtils.hasText(blogUrl)) this.blogUrl = blogUrl;
        if (StringUtils.hasText(introduceComment)) this.introduceComment = introduceComment;
        if (StringUtils.hasText(profileImageUrl)) this.profileImageUrl = profileImageUrl;
    }

}


