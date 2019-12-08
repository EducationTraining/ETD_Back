package com.etd.etdservice.dao;

import com.etd.etdservice.bean.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
    @Autowired
    private UserDAO userDAO;

    @Test
    public void testGetUserById() {
        User queryUser = mockUser();
        User resUser = userDAO.queryUserById(queryUser.getId());
        assertEquals(resUser.getName(), queryUser.getName());
    }

    /**
     * Mock a persistent User object
     * @return a User object
     */
    public static User mockUser() {
        User user = new User();
        user.setName("testName");
        user.setId(1);
        return user;
    }
}
