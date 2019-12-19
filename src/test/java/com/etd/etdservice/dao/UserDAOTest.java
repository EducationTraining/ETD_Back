package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Admin;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.utils.MD5Util;
import com.etd.etdservice.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private TeacherDAO teacherDAO;
	@Autowired
	private AdminDAO adminDAO;

	public static Student mockStudent() {
		Student student = new Student();
		student.setUserName(StringUtil.generateRandomString("testUserName"));
		student.setSessionKey(StringUtil.generateRandomString("testSessionKey"));
		student.setCreateTime(new Date());
		return student;
	}

	public static Teacher mockTeacher() {
		Teacher teacher = new Teacher();
		teacher.setUserName(StringUtil.generateRandomString("testUserName"));
		teacher.setSessionKey(StringUtil.generateRandomString("testSessionKey"));
		teacher.setCreateTime(new Date());
		return teacher;
	}

	public static Admin mockAdmin() {
		Admin admin=new Admin();
		admin.setUserName(StringUtil.generateRandomString("testUserName"));
		//student.setUserName(StringUtil.generateRandomString("testUserName"));
		admin.setSessionKey(StringUtil.generateRandomString("testSessionKey"));
		admin.setCreateTime(new Date());
		admin.setEmail(StringUtil.generateRandomString("testEmail"));
		admin.setPhone(StringUtil.generateRandomString("0575"));
		return admin;
	}

	@Test
	public void testStudent() {
		Student student = mockStudent();
		studentDAO.create(student);

		Student resStudent = studentDAO.queryByUserName(student.getUserName());
		assertTrue(resStudent.getId() > 0);
		assertEquals(resStudent.getUserName(), student.getUserName());

		resStudent = studentDAO.queryById(resStudent.getId());
		assertEquals(resStudent.getSessionKey(), student.getSessionKey());

		String updatedUserName = "updated" + resStudent.getUserName();
		resStudent.setUserName(updatedUserName);
		studentDAO.update(resStudent);
		resStudent = studentDAO.queryById(resStudent.getId());
		assertEquals(resStudent.getUserName(), updatedUserName);
	}

	@Test
	public void testTeacher() {
		Teacher teacher = mockTeacher();
		teacherDAO.create(teacher);

		Teacher resTeacher = teacherDAO.queryByUserName(teacher.getUserName());
		assertTrue(resTeacher.getId() > 0);
		assertEquals(resTeacher.getUserName(), teacher.getUserName());

		resTeacher = teacherDAO.queryById(resTeacher.getId());
		assertEquals(resTeacher.getSessionKey(), teacher.getSessionKey());

		String updatedUserName = "updated" + resTeacher.getUserName();
		resTeacher.setUserName(updatedUserName);
		teacherDAO.update(resTeacher);
		resTeacher = teacherDAO.queryById(resTeacher.getId());
		assertEquals(resTeacher.getUserName(), updatedUserName);
	}

	@Test
	public void testAdmin() {
		//测试create和queryByUserName方法
		Admin admin = mockAdmin();
		adminDAO.create(admin);
		Admin resAdmin = adminDAO.queryByUserName(admin.getUserName());
		assertTrue(resAdmin.getId() > 0);
		assertEquals(resAdmin.getUserName(), admin.getUserName());
		//测试queryById方法
		resAdmin = adminDAO.queryById(resAdmin.getId());
		assertEquals(resAdmin.getSessionKey(), admin.getSessionKey());
		//测试queryBySessionKey方法
		resAdmin  =adminDAO.queryBySessionKey(admin.getSessionKey());
		assertEquals(resAdmin.getUserName(), admin.getUserName());
		//测试update方法
		String updatedUserName = "updated" + resAdmin.getUserName();
		resAdmin.setUserName(updatedUserName);
		adminDAO.update(resAdmin);
		resAdmin = adminDAO.queryById(resAdmin.getId());
		assertEquals(resAdmin.getUserName(), updatedUserName);
	}
}
