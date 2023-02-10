package com.example.servicehub.security.authentication;

import java.util.Optional;

public abstract class ClientPrincipalUtil {

    public static Optional<Long> getClientIdFrom(ClientPrincipal clientPrincipal){
        if(clientPrincipal == null) return Optional.empty();
        return Optional.ofNullable(clientPrincipal.getId());
    }

    private ClientPrincipalUtil(){
    }

}
