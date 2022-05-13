package ru.kata.spring.boot_security.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        log.info("Method UserServiceImpl.getAllUsers provided Users");
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public User getUserById(long id) {
        log.info("Method UserServiceImpl.getUserById provided {} user", id);
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void add(User user) {
        log.info("Method UserServiceImpl.add added {}", user.toString());
        userDao.add(user);
    }

    @Override
    @Transactional
    public void update(long id, User user) {
        log.info("Method UserServiceImpl.update update {} user {}", id, user.toString());
        userDao.update(id, user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Method UserServiceImpl.delete delete {} user", id);
        userDao.delete(id);
    }


    @Override
    public User findByUserName(String username) {
        log.info("Method UserServiceImpl.findByUserName provided {}", username);
        return userDao.findByUserName(username);
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);
        if (user == null) {
            log.error("User {} not found", username);
        }
        log.info("Method UserServiceImpl.loadUserByUsername handed over to Security name {}", username);
        return user;
    }

}
