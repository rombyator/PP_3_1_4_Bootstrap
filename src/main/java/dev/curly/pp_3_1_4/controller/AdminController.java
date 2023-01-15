package dev.curly.pp_3_1_4.controller;

import dev.curly.pp_3_1_4.exceptions.UserEmailAlreadyInUse;
import dev.curly.pp_3_1_4.model.User;
import dev.curly.pp_3_1_4.service.RoleService;
import dev.curly.pp_3_1_4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String listUsers(Model model, @AuthenticationPrincipal User user, @ModelAttribute("newUser") User newUser) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("currentUser", user);
        // Add completely new user or from redirect
        model.addAttribute("newUser", newUser == null ? User.newUser() : newUser);

        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes attributes) {
        try {
            userService.add(user);
        } catch (UserEmailAlreadyInUse e) {
            attributes.addFlashAttribute("newUser", user);
            return "redirect:/admin?emailInUse#user-add";
        }

        return "redirect:/admin";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);

        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);

        return "redirect:/admin";
    }
}
