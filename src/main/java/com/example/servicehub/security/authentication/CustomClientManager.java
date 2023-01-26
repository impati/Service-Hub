package com.example.servicehub.security.authentication;

import com.example.servicehub.domain.Client;
import com.example.servicehub.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@RequiredArgsConstructor
public class CustomClientManager implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("유효하지 않은 사용자 입니다"));

        return ClientContext.of(client);
    }
}
