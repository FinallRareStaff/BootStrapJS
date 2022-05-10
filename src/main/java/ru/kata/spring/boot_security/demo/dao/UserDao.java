package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(long id);
    void delete(long id);
    void update(long id, User user);
    void add(User user);
    User findByUserName(String username);
}

