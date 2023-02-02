package com.example.servicehub.security.authentication.login;

import com.example.servicehub.domain.ProviderType;

import java.util.Map;

public abstract class ProviderUserFactory {

    public static ProviderUser create(ProviderType providerType , Map<String, Object> attributes){
        switch (providerType){
            case KEYCLOAK: return new KeycloakUser(attributes,providerType);
            case KAKAO: return new KakaoUser(attributes,providerType);
            case NAVER: return new NaverUser(attributes,providerType);
            case GOOGLE: return new GoogleUser(attributes,providerType);
        }
        throw new IllegalStateException("Provider 타입을 잘못 넘겼습니다.");
    }

    private ProviderUserFactory(){
    }
}
