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
        Thread.sleep(100);
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
        Collection<Role> roleCollection = new HashSet<>();

        checkRolesAgainstFrontEnd(data, roleCollection);

        User user = new User(data.get("name"),
                data.get("nickname"),
                Integer.parseInt(data.get("ladder")),
                data.get("email"),
                givePassword(data),
                roleCollection);

        userService.add(user);
        log.info("CONTROLLER METHOD POST - createUser , POST on Admin_page");
        return user;
    }

    @PatchMapping(value = "/admin/editUser")
    public User updateUser(@RequestBody HashMap<String, String> data) {
        Collection<Role> roleCollection = new HashSet<>();

        long userId = Long.parseLong(data.get("id"));

        roleCollection = getRoles(data, roleCollection, userId);

        User user = new User(userId,
                data.get("name"),
                data.get("nickname"),
                Integer.parseInt(data.get("ladder")),
                data.get("email"),
                givePassword(data),
                roleCollection);

        userService.update(userId, user);
        log.info("CONTROLLER METHOD PATCH - updateUser , PATCH on Admin_page");
        return user;
    }


    //BCryptEncoder
    private String givePassword(HashMap<String, String> data) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(data.get("password"));
    }

    //Add Roles
    private Collection<Role> getRoles(HashMap<String, String> data, Collection<Role> roleCollection, long userId) {
        if (data.get("roles").isEmpty()) {
            User userBefore = userService.getUserById(userId);
            roleCollection = userBefore.getRoles();
        } else {
            checkRolesAgainstFrontEnd(data, roleCollection);
        }
        return roleCollection;
    }

    //List<Role>
    private void checkRolesAgainstFrontEnd(HashMap<String, String> data, Collection<Role> roleCollection) {
        var feRoleIds = Stream
                .of(data.get("roles").split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        var validRoles = roleService.getAllRoles().stream()
                .filter(dbRole -> feRoleIds.contains(dbRole.getId()))
                .collect(Collectors.toList());
        roleCollection.addAll(validRoles);
    }
}
