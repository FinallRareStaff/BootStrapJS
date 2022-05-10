package ru.kata.spring.boot_security.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSample {

    @Autowired
    private RoleService roleService;

    private long id;
    private String name;
    private String nickName;
    private int ladder;
    private String email;
    private String username;
    private String password;
    private Set<String> roles;

    public UserSample() {
        roles = new HashSet<>();
    }


    public UserSample(long id, String name, String nickName, int ladder, String email, String username, String password, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.ladder = ladder;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        for (Role role : roleService.getAllRoles()) {
            if (roles.contains(role.getRole())) {
                this.roles.add(role.getRole());
            }
        }
    }

    public UserSample(User user) {
        id = user.getId();
        name = user.getName();
        nickName = user.getNickName();
        ladder = user.getLadder();
        email = user.getEmail();
        username = user.getUsername();
        password = "";
        roles = user.getRoles()
                .stream()
                .map(r -> r.getRole())
                .collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLadder() {
        return ladder;
    }

    public void setLadder(int ladder) {
        this.ladder = ladder;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles
                .stream()
                .map(r -> r.getRole())
                .collect(Collectors.toSet());
    }

    public boolean hasAdminRole() {
        return roles.contains("ROLE_ADMIN");
    }

    public boolean hasUserRole() {
        return roles.contains("ROLE_USER");
    }
}
