package com.example.servicehub.security.authentication.login;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.util.ProjectUtils;

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
                providerType,
                ProjectUtils.getDomain() + "/client",
                ProjectUtils.getDomain() +"/profile/default.png"
        );
    }

    @Override
    protected String getAttributeByName(String attributeName) {
        return getUsernameAttribute("response").get(attributeName).toString();
    }
}
