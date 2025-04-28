package com.chamados.API.controllers;

import com.chamados.API.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {
    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String HomePage(Authentication authentication) {
        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getUser());
        }
        return "Olá " + authentication.getName();
    }

}
