package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UsersController {

    private final UserService userService;
    private final RoleService roleService;

    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.loadUserByUsername(principal.getName());
            Set<String> roles = AuthorityUtils.authorityListToSet(user.getAuthorities());
            boolean roleAdmin = roles.contains("ROLE_ADMIN");
            model.addAttribute("username", principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("roleAdmin", roleAdmin);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "users/user";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        if (principal != null) {
            String stringRoleList = new String();
            User user = userService.loadUserByUsername(principal.getName());
            model.addAttribute("username", principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("rolesAll", roleService.getAllRoles());
            model.addAttribute("stringRoleList", stringRoleList);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "users/admin";
    }

}
