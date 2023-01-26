package com.example.servicehub.web.util;

import com.example.servicehub.security.authentication.ClientContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class ClientIdGetter {

    public static Long getIdForm(UsernamePasswordAuthenticationToken token){
        ClientContext clientContext = (ClientContext) token.getPrincipal();
        return clientContext.getClient().getId();
    }

    private ClientIdGetter(){
    }

}
