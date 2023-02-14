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

        Map<String,Object> attributes = oAuth2User.getAttributes();

        ProviderUser providerUser = ProviderUserFactory.create(getProviderType(userRequest), oAuth2User.getAttributes());

        Optional<Client> client = clientRepository.findByUserId(providerUser.getId());

        if(client.isPresent())
            return ClientPrincipal.create(client.get(),attributes);

        return returnAfterSaveClient(providerUser,attributes);
    }

    private ProviderType getProviderType(OAuth2UserRequest userRequest){
        return ProviderType.valueOf(userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase());
    }

    private OAuth2User returnAfterSaveClient(ProviderUser providerUser,Map<String,Object> attributes){
        Client client = providerUser.toClient();
        clientRepository.save(client);
        return ClientPrincipal.create(client,attributes);
    }

}
