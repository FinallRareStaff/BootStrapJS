package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.service.RoleService;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 150)
    private long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickName;

    @Column(name = "ladder", nullable = false, length = 50, unique = true)
    private int ladder;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(UserSample userSample, RoleService roleService) {
        id = userSample.getId();
        name = userSample.getName();
        nickName = userSample.getNickName();
        ladder = userSample.getLadder();
        email = userSample.getEmail();
        username = userSample.getUsername();
        password = userSample.getPassword();
        roles = new HashSet<>();
        for (Role role : roleService.getAllRoles()) {
            if (userSample.getRoles().contains(role.getRole())) {
                roles.add(role);
            }
        }
    }

    public void update(UserSample userSample, RoleService roleService) {
        name = userSample.getName();
        nickName = userSample.getNickName();
        ladder = userSample.getLadder();
        email = userSample.getEmail();
        username = userSample.getUsername();
        roles = new HashSet<>();
        if (!userSample.getPassword().equals("")) {
            password = userSample.getPassword();
            password = new BCryptPasswordEncoder().encode(password);
        }
        for (Role role : roleService.getAllRoles()) {
            if (userSample.getRoles().contains(role.getRole())) {
                roles.add(role);
            }
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public void setNickName(String surname) {
        this.nickName = surname;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name)
                && Objects.equals(nickName, user.nickName)
                && Objects.equals(ladder, user.ladder)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nickName, ladder, email);
    }

    @Override
    public String toString() {
        return "Id = " + id +
                "\nName = " + name +
                "\nNickName = " + nickName +
                "\nLadder = " + ladder +
                "\nEmail = " + email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




}
