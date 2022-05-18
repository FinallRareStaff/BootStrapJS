package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        if (principal != null) {
        User user = userService.loadUserByUsername(principal.getName());
            Set<String> roles = AuthorityUtils.authorityListToSet(user.getAuthorities());
            model.addAttribute("roleAdmin", roles.contains("ROLE_ADMIN"));
        }
        return "users/user";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        if (principal != null) {
            String stringRoleList = "";
            model.addAttribute("stringRoleList", stringRoleList);
        }
        return "users/admin";
    }

}
