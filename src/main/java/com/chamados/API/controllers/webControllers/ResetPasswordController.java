package com.chamados.API.controllers.webControllers;

import com.chamados.API.entities.User;
import com.chamados.API.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/resetar-senha")
    public String showResetForm(Model model) {
        model.addAttribute("error", null);
        return "reset-password";
    }

    @PostMapping("/resetar-senha")
    @Transactional
    public String processResetPassword(
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha,
            @RequestParam String confirmarSenha,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        User user = (User) authentication.getPrincipal();
        User storedUser = userRepository.findById(user.getId()).orElse(null);

        if (storedUser == null || !passwordEncoder.matches(senhaAtual, storedUser.getPassword())) {
            model.addAttribute("error", "Senha atual incorreta.");
            return "reset-password";
        }

        if (!novaSenha.equals(confirmarSenha)) {
            model.addAttribute("error", "As novas senhas não coincidem.");
            return "reset-password";
        }

        storedUser.setPassword(passwordEncoder.encode(novaSenha));
        storedUser.setIsChangePasswordNextLogin(false);
        userRepository.save(storedUser);
        redirectAttributes.addFlashAttribute("success", "Senha atualizada com sucesso!");

        return "redirect:/login";
    }
}
