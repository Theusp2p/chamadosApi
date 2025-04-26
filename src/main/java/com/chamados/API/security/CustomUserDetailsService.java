package com.chamados.API.security;

import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.chamados.API.entities.User userFound = userService.GetByUsername(username);

        if (userFound == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return User.builder()
                .username(userFound.getUsername())
                .password(userFound.getPassword())
                .roles(userFound.getRole().toString())
                .build();

    }
}
