package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.User;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUserViewController {

    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping
    public String listUsers(
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        List<User> users = userService.searchUsers(search);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin/users/list";
    }

    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/users/new";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("departments", departmentService.findAll());
        return "admin/users/edit";
    }

    @PostMapping("/salvar")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(required = false) String action,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();

            if (user.getId() == null) {
                // Novo usuário - seta createdBy
                user.setCreatedBy(currentUser);
            } else {
                // Usuário existente - seta lastModifiedBy
                user.setLastModifiedBy(currentUser.getUsername());
            }
        }

        if ("deactivate".equals(action)) {
            user.setIsActive(false);
        } else if ("activate".equals(action)) {
            user.setIsActive(true);
        }

        userService.insertUser(user);
        redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
        return "redirect:/admin/usuarios";
    }
}
