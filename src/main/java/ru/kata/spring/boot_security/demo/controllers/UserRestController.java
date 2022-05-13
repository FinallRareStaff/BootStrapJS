package ru.kata.spring.boot_security.demo.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/getUsers")
    @SneakyThrows
    public List<User> giveAllUsers() {
        log.info("CONTROLLER METHOD GET - giveAllUsers");
        Thread.sleep(500);
        return userService.getAllUsers();
    }

    @GetMapping(value = "/admin/getRoles")
    public List<Role> giveAllRoles() {
        log.info("CONTROLLER METHOD GET - giveAllRoles");
        return roleService.getAllRoles();
    }

    @GetMapping(value = "/admin/userFromId/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        if (id == null) {
            log.error("Method UserRestController.getUserById {} send BAD REQUEST!", id);
        }
        log.info("CONTROLLER METHOD GET - getUserById for id = {}", id);
        return userService.getUserById(id);
    }

    @GetMapping(value = "/user/userLogined")
    public User getUserLogined(Principal principal) {
        log.info("CONTROLLER METHOD GET - getUserLogined");
        return userService.loadUserByUsername(principal.getName());
    }

    @DeleteMapping(value = "/admin/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        if (id == null) {
            log.error("Method UserRestController.deleteUser {} send BAD REQUEST!", id);
        }
        log.info("CONTROLLER METHOD DELETE - deleteUser for id = {}", id);
        userService.delete(id);
    }

    @PostMapping(value = "/admin/createUser")
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
        log.info("CONTROLLER METHOD POST - createUser , POST on Admin_page");
        return user;
    }

    @PatchMapping(value = "/admin/editUser")
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
        log.info("CONTROLLER METHOD PATCH - updateUser , PATCH on Admin_page");
        return user;
    }
}
