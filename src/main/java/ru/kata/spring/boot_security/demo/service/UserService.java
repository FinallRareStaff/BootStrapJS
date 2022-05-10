package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(long id);
    void add(User user);
    void update(long id, User user);
    void delete(long id);
    void updateUser(User user, String stringRoleList);
    User findByUserName(String username);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}