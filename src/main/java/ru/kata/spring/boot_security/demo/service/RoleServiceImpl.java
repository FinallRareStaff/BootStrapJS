package ru.kata.spring.boot_security.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getAllRoles() {
        log.info("Method RoleServiceImpl.getAllRoles provided Roles");
        return roleDao.getAllRoles();
    }
}
