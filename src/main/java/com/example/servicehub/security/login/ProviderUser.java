package com.example.servicehub.security.login;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;

import java.util.Map;

public abstract class ProviderUser {

    protected final Map<String,Object>  attributes;
    protected final ProviderType providerType;

    protected ProviderUser(Map<String, Object> attributes, ProviderType providerType) {
        this.attributes = attributes;
        this.providerType = providerType;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract Client toClient();

    protected String getAttributeByName(String attributeName){
        return attributes.get(attributeName).toString();
    }

    protected Map<String,Object> getUsernameAttribute(String usernameAttribute){
        try{
            return (Map<String, Object>) attributes.get(usernameAttribute);
        }catch (ClassCastException e){
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }

}
