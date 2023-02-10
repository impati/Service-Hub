package com.example.servicehub.security.authentication;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientPrincipal implements OAuth2User, UserDetails {

    private final Long id;
    private final String username;
    private String nickname;
    private final String email;
    private final ProviderType providerType;
    private final RoleType roleType;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private final String blogUrl;
    private final String profileImageUrl;
    private final String introduceComment;


    @Override
    public String getPassword() {
        return null;
    }

    @Override
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
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return nickname;
    }


    public String getEmail(){return email;}


    public void editNickname(String nickname){
        this.nickname = nickname;
    }

    public static ClientPrincipal create(Client client) {
        return new ClientPrincipal(
                client.getId(),
                client.getUsername(),
                client.getNickname(),
                client.getEmail(),
                client.getProviderType(),
                client.getRoleType(),
                Collections.singletonList(new SimpleGrantedAuthority(client.getRoleType().getName())),
                client.getBlogUrl(),
                client.getProfileImageUrl(),
                client.getIntroduceComment()
        );
    }


    public static ClientPrincipal create(Client client, Map<String, Object> attributes) {
        ClientPrincipal clientPrincipal = create(client);
        clientPrincipal.setAttributes(attributes);
        return clientPrincipal;
    }


    private ClientPrincipal(Long id, String username, String nickname,
                            String email, ProviderType providerType, RoleType roleType,
                            Collection<GrantedAuthority> authorities, String blogUrl, String profileImageUrl, String introduceComment) {
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

}


