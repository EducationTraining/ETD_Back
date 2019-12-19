package com.etd.etdservice.service;


import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.Admin;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.AdminDAO;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.TeacherDAO;
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
import javax.validation.constraints.Max;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Test
    public void testRegister() {
        // 注册一条管理员记录
        ResponseRegister responseRegister;
        Integer MAX_VALUE = 100000000;
        String name = "" + new Random().nextInt(MAX_VALUE);
        String password = "" + new Random().nextInt(MAX_VALUE);
        //调用service层方法
        responseRegister = adminService.register(name,password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(), MD5Util.getMD5String(name + password));

    }

    @Test
    public void testGetLoginInfo() {
        // 新建一条管理员记录
        ResponseRegister responseRegister;
        Integer MAX_VALUE = 100000000;
        String name = "" + new Random().nextInt(MAX_VALUE);
        String password = "" + new Random().nextInt(MAX_VALUE);
        adminService.register(name, password);
        // 调用service层的管理员登录方法
        responseRegister = adminService.getLoginInfo(name, password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(), MD5Util.getMD5String(name + password));

    }

    @Test
    public void testGetAdminInfo() {
        // 插入一条管理员记录
        ResponseGetAdmin responseGetAdmin;
        Integer MAX_VALUE = 100000000;
        String userName = "" + new Random().nextInt(MAX_VALUE);
        String password = "" + new Random().nextInt(MAX_VALUE);
        adminService.register(userName, password);
        // 调用service层方法
        responseGetAdmin = adminService.getAdminInfo(MD5Util.getMD5String(userName + password));
        assertEquals(userName,responseGetAdmin.getUserName());
    }

    @Test
    public void testGetAllCourses() {
        // 先插入一条教师信息，并取得该教师的完整信息（包括id）
        Course course;
        Integer MAX_VALUE = 100000000;
        Teacher teacher = new Teacher();
        String nameOfTeacher = "" + new Random().nextInt(MAX_VALUE);
        String sessionKeyOfTeacher = "" + new Random().nextInt(MAX_VALUE);
        teacher.setUserName(nameOfTeacher);
        teacher.setSessionKey(sessionKeyOfTeacher);
        teacher.setCreateTime(new Date());
        teacherDAO.create(teacher);
        teacher = teacherDAO.queryBySessionKey(sessionKeyOfTeacher);
        // 插入一条管理员信息，并取得该管理员的sessionKey
        String adminName = "" + new Random().nextInt(MAX_VALUE);
        String adminPassWord = "" + new Random().nextInt(MAX_VALUE);
        String sessionKey = adminService.register(adminName,adminPassWord).getSessionKey();
        // 插入10条课程信息
        for(int i = 0; i<10; i++) {
            course = new Course();
            course.setName("" + i);
            course.setTeacherId(teacher.getId());
            course.setCreateTime(new Date());
            courseDAO.create(course);
        }
        // 调用service层getAllCourses方法
        ResponseGetCourses courses = adminService.getAllCourses(sessionKey);
        // 判断返回结果是否符合预期
        assertTrue(courses.isSuccess());
    }

    @Test
    public void testUpdateCourseStatus() {
        // 插入一条管理员信息
        Integer MAX_VALUE = 100000000;
        String adminName = "" + new Random().nextInt(MAX_VALUE);
        String adminPassWord = "" + new Random().nextInt(MAX_VALUE);
        String sessionKeyOfAdmin = adminService.register(adminName,adminPassWord).getSessionKey();
        // 插入一条教师信息,并获得该教师的完整信息
        Teacher teacher = new Teacher();
        String nameOfTeacher = "" + new Random().nextInt(MAX_VALUE);
        String sessionKeyOfTeacher = "" + new Random().nextInt(MAX_VALUE);
        teacher.setUserName(nameOfTeacher);
        teacher.setSessionKey(sessionKeyOfTeacher);
        teacher.setCreateTime(new Date());
        teacherDAO.create(teacher);
        teacher = teacherDAO.queryBySessionKey(sessionKeyOfTeacher);
        // 插入一门课程信息，并获得该课程完整信息
        Course course = new Course();
        String nameOfCourse = "" + new Random().nextInt(MAX_VALUE);
        String coursesNum = "" + new Random().nextInt(MAX_VALUE);
        course.setName(nameOfCourse);
        course.setTeacherId(teacher.getId());
        course.setCreateTime(new Date());
        course.setCourseNum(coursesNum);
        courseDAO.create(course);
        course = courseDAO.queryByCourseNum(coursesNum);
        // 调用service层方法，判断返回值是否符合预期
        assertTrue(adminService.updateCourseStatus(course.getId(),2,sessionKeyOfAdmin).isSuccess());
        assertTrue(adminService.updateCourseStatus(course.getId(),1,sessionKeyOfAdmin).isSuccess());
    }
}
