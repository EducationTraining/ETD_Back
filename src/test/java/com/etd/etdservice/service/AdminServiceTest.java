package com.etd.etdservice.service;


import com.etd.etdservice.bean.users.Admin;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.serivce.AdminService;
import com.etd.etdservice.serivce.UserService;
import com.etd.etdservice.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;


    @Test
    public void testRegister()
    {
        ResponseRegister responseRegister;
        String name="testName"+new Random().nextInt(100000);
        String password="testPasswd"+new Random().nextInt(100000);
        responseRegister=adminService.register(name,password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(), MD5Util.getMD5String(name + password));

    }

    @Test
    public void testGetLoginInfo()
    {
        ResponseRegister responseRegister;
        String name="testName"+new Random().nextInt(100000);
        String password="testPasswd"+new Random().nextInt(100000);
        adminService.register(name,password);
        responseRegister=adminService.getLoginInfo(name,password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(),MD5Util.getMD5String(name + password));

    }

    @Test
    public void testGetAdminInfo()
    {
        ResponseGetAdmin responseGetAdmin;
        String userName="testName"+new Random().nextInt(100000);
        String password=""+new Random().nextInt(100000000);

        adminService.register(userName,password);

        responseGetAdmin=adminService.getAdminInfo(MD5Util.getMD5String(userName + password));
        assertEquals(userName,responseGetAdmin.getUserName());
    }
}
