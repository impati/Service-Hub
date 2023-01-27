package com.example.servicehub.web.util;

import com.example.servicehub.security.authentication.ClientContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class ClientIdGetter {


    public static Long getIdFrom(UsernamePasswordAuthenticationToken token){
        try{
            return getId(token);
        }catch (NullPointerException e){
            return null;
        }
    }

    private static Long getId(UsernamePasswordAuthenticationToken token){
        ClientContext clientContext = (ClientContext) token.getPrincipal();
        return clientContext.getClient().getId();
    }

    private ClientIdGetter(){
    }

}
