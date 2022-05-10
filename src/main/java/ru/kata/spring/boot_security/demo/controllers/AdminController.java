package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserSample;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("sampleUser", new UserSample());
        model.addAttribute("user", userService.findByUserName(authentication.getName()));
        model.addAttribute("users", userService.getAllUsers()
                .stream()
                .map(u -> new UserSample(u))
                .collect(Collectors.toList()));
        return "admin/user_table";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") UserSample user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/user_new";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") UserSample user) {
        userService.add(new User(user, roleService));
        return "redirect:/admin/users";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("user") UserSample user) {
        User eUser = userService.getUserById(id);
        eUser.update(user, roleService);
        userService.update(id, eUser);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}
