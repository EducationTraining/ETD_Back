package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private TeacherDAO teacherDAO;

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
}
