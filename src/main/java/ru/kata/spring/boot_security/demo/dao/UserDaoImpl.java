package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void delete(long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public void update(long id, User user) {
        entityManager.merge(user);
    }

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByUserName(String username) {
        return (User) entityManager
                .createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email=: username")
                .setParameter("username", username)
                .getSingleResult();
    }
}
