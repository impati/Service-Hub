package com.example.servicehub.security.authentication;

import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.repository.ClientRepository;
import com.example.servicehub.security.authentication.login.ProviderUser;
import com.example.servicehub.security.authentication.login.ProviderUserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ClientRepository clientRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        ProviderUser providerUser = ProviderUserFactory.create(getProviderType(userRequest), oAuth2User.getAttributes());

        Optional<Client> optionalClient = clientRepository.findByUserId(providerUser.getId());

        return returnPrincipalOrSave(optionalClient,oAuth2User.getAttributes(),providerUser);
    }

    private ProviderType getProviderType(OAuth2UserRequest userRequest){
        return ProviderType.valueOf(userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase());
    }

    private OAuth2User returnPrincipalOrSave(Optional<Client> optionalClient, Map<String,Object> attributes,ProviderUser providerUser){
        if(optionalClient.isPresent()) return ClientPrincipal.create(optionalClient.get(),attributes);
        Client client = providerUser.toClient();
        clientRepository.save(client);
        return ClientPrincipal.create(client,attributes);
    }
}
