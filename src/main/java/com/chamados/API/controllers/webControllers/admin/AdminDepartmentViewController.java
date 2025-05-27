package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.Department;
import com.chamados.API.services.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/departamentos")
public class AdminDepartmentViewController {


    private final DepartmentService departmentService;

    @GetMapping

        public String findAllDepartments(Model model) {

            model.addAttribute("activePage", "departamentos");
            model.addAttribute("departments", departmentService.findAllDepartments());
            return "admin/departments/list";
        }

    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("activePage", "departamentos");
        return "admin/departments/new";
    }

    @PostMapping("/salvar")
    public String saveUser(@ModelAttribute Department department,
                           @RequestParam(required = false) String action,
                           RedirectAttributes redirectAttributes) {

        departmentService.insertDepartment(department);
        redirectAttributes.addFlashAttribute("success", "Departamento cadastrado com sucesso!");
        return "redirect:/admin/departamentos";
    }



    @GetMapping("/{id}/editar")
    public String editDepartment(Model model, @PathVariable Integer id) {
        Department department = departmentService.findById(id);

        model.addAttribute("department", department);
        model.addAttribute("activePage", "departamentos");
        return "admin/departments/edit";
    }

    @PostMapping("/{id}/atualizar")
    @Transactional
    public String saveEdit(@PathVariable Integer id, @ModelAttribute("department") Department updatedDepartment, RedirectAttributes redirectAttributes) {
        departmentService.updateDepartment(id, updatedDepartment);
        redirectAttributes.addFlashAttribute("sucess", "Departmento atualizado com sucesso!");
        return "redirect:/admin/departamentos";
    }

}
