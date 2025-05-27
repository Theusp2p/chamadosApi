package com.chamados.API.security;

import com.chamados.API.entities.User;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User userFound = userService.getByUsername(username.toLowerCase());

        if (!userFound.getIsActive()) {
            throw new DisabledException("Usuário inativo");
        }

        if (userFound == null) {
            throw getErrorUserNotFound();
        }

        String encryptedPassword = userFound.getPassword();

        boolean PasswordsMatch = passwordEncoder.matches(password, encryptedPassword);

        if (PasswordsMatch) {
            return new CustomAuthentication(userFound);
        }

        throw getErrorUserNotFound();

    }


    private UsernameNotFoundException getErrorUserNotFound() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
