package dev.curly.pp_3_1_4.controller;

import dev.curly.pp_3_1_4.model.User;
import dev.curly.pp_3_1_4.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model, Principal principal) {
        var user = Optional
            .ofNullable(principal)
            .map(Principal::getName)
            .map(userService::loadUserByUsername)
            .orElse(User.anonymousUser());
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
