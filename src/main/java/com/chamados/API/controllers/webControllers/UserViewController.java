package com.chamados.API.controllers.webControllers;

import com.chamados.API.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {

    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        if (authentication instanceof CustomAuthentication customAuth) {
            model.addAttribute("user", customAuth.getUser());
        }
        return "user/dashboard";
    }

}
