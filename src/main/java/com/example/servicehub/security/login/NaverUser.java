package com.example.servicehub.security.login;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;

import java.util.Map;

public class NaverUser extends ProviderUser{

    protected NaverUser(Map<String, Object> attributes, ProviderType providerType) {
        super(attributes, providerType);
    }

    @Override
    public String getId() {
        return getAttributeByName("id");
    }

    @Override
    public String getName() {
        return getAttributeByName("name");
    }

    @Override
    public String getEmail() {
        return getAttributeByName("email");
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

    @Override
    protected String getAttributeByName(String attributeName) {
        return getUsernameAttribute("response").get(attributeName).toString();
    }
}
