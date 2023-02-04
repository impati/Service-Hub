package com.example.servicehub.security.authentication.login;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;

import java.util.Map;

public class GoogleUser extends ProviderUser{

    protected GoogleUser(Map<String, Object> attributes, ProviderType providerType) {
        super(attributes, providerType);
    }

    @Override
    public String getId() {
        return getAttributeByName("sub");
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

}
