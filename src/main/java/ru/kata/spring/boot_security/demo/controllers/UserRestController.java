package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/json/allUsers")
    public List<User> giveAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/admin/json/allRoles")
    public List<Role> giveAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping(value = "/admin/json/userFromId/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/user/json/userAuthorized")
    public User getUserLogined(Principal principal) {
        return userService.loadUserByUsername(principal.getName());
    }

    @DeleteMapping(value = "/admin/json/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }

    @PostMapping(value = "/admin/json/createUser")
    public User createUser(@RequestBody HashMap<String, String> data) {
        User user = new User();
        user.setName(data.get("name"));
        user.setNickname(data.get("nickname"));
        user.setLadder(Integer.parseInt(data.get("ladder")));
        user.setEmail(data.get("email"));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCryptPassword = bCryptPasswordEncoder.encode(data.get("password"));
        user.setPassword(bCryptPassword);

        Collection<Role> roleCollection = new HashSet<>();

        var feRoleIds = Stream
                .of(data.get("roles").split(","))
                .map(Long :: valueOf)
                .collect(Collectors.toList());

        var validRoles = roleService.getAllRoles().stream()
                .filter(dbRole -> feRoleIds.contains(dbRole.getId()))
                .collect(Collectors.toList());
        roleCollection.addAll(validRoles);

        user.setRoles(roleCollection);
        userService.add(user);
        return user;
    }

    @PatchMapping(value = "/admin/json/editUser")
    public User updateUser(@RequestBody HashMap<String, String> data) {
        User user = new User();
        long userId = Long.parseLong(data.get("id"));
        user.setId(userId);
        user.setName(data.get("name"));
        user.setNickname(data.get("nickname"));
        user.setLadder(Integer.parseInt(data.get("ladder")));
        user.setEmail(data.get("email"));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCryptPasswod = bCryptPasswordEncoder.encode(data.get("password"));
        user.setPassword(bCryptPasswod);

        Collection<Role> roleCollection = new HashSet<>();

        if (data.get("roles").isEmpty()) {
            User userBefore = userService.getUserById(userId);
            roleCollection = userBefore.getRoles();
        } else {
            var feRoleIds = Stream
                    .of(data.get("roles").split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            var validRoles = roleService.getAllRoles().stream()
                    .filter(dbRole -> feRoleIds.contains(dbRole.getId()))
                    .collect(Collectors.toList());
            roleCollection.addAll(validRoles);
        }
        user.setRoles(roleCollection);
        userService.update(userId, user);
        return user;
    }
}
