package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(long id);
    void add(User user);
    void update(long id, User user);
    void delete(long id);
    User findByUserName(String username);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
}