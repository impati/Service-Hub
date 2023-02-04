package com.example.servicehub.security.authentication.login;


import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;

import java.util.Map;

public class KakaoUser extends ProviderUser {

    protected KakaoUser(Map<String, Object> attributes, ProviderType providerType) {
        super(attributes, providerType);
    }

    @Override
    public String getId() {
        return getAttributeByName("id");
    }

    @Override
    public String getName() {
        return getProfile().get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return getKakaoAccount().get("email").toString();
    }

    @Override
    public Client toClient() {
        return Client.of(
                getId(),
                getEmail(),
                getName(),
                getEmail(),
                "ROLE_USER",
                providerType
        );
    }


    private Map<String,Object> getProfile(){
        try {
            return (Map<String, Object>) getKakaoAccount().get("profile");
        }catch (ClassCastException e){
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }

    private Map<String,Object> getKakaoAccount(){
        try {
            return (Map<String, Object>) attributes.get("kakao_account");
        }catch (ClassCastException e){
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }

}
