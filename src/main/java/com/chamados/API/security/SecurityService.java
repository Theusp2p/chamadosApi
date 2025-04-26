package com.chamados.API.security;

import com.chamados.API.entities.User;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public User GetUsernameLogged() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth instanceof CustomAuthentication customAuth) {
        return customAuth.getUser();
    }
    return null;
    }

}
