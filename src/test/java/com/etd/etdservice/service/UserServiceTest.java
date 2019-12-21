package com.etd.etdservice.service;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.requests.RequestUpdateStudent;
import com.etd.etdservice.bean.users.requests.RequestUpdateTeacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.dao.UserDAOTest;
import com.etd.etdservice.serivce.UserService;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.core.AllOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
	@Autowired
	private UserService userService;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private TeacherDAO teacherDAO;

	@Test
	public void testStudentRegister() {
		RequestRegister request = mockRegisterRequest();
		log.info("register with userName: " + request.getUserName() +
				"with pwd: " + request.getPassword());

		ResponseRegister response = userService.registerForStudent(request.getUserName(), request.getPassword());
		assertTrue(response.isSuccess());

		// Test repeat user name
		response = userService.registerForStudent(request.getUserName(), request.getPassword());
		assertFalse(response.isSuccess());
	}

	@Test
	public void testTeacherRegister() {
		RequestRegister request = mockRegisterRequest();
		log.info("register with userName: " + request.getUserName() +
				"with pwd: " + request.getPassword());

		ResponseRegister response = userService.registerForTeacher(request.getUserName(), request.getPassword());
		assertTrue(response.isSuccess());

		// Test repeat user name
		response = userService.registerForTeacher(request.getUserName(), request.getPassword());
		assertFalse(response.isSuccess());
	}

	@Test
	public void testStudentLogin() {
		RequestRegister request = mockRegisterRequest();
		log.info("register with userName: " + request.getUserName() +
				"with pwd: " + request.getPassword());
		userService.registerForStudent(request.getUserName(), request.getPassword());

		ResponseRegister response = userService.getStudentLoginInfo(request.getUserName(), request.getPassword());
		assertTrue(response.isSuccess());

		// Test invalid password
		RequestRegister request1 = mockRegisterRequest();
		request1.setUserName(request.getUserName());
		response = userService.getStudentLoginInfo(request1.getUserName(), request1.getPassword());
		log.info(response.getErrMsg());
		assertFalse(response.isSuccess());

		// Test invalid username
		RequestRegister request2 = mockRegisterRequest();
		request2.setPassword(request.getPassword());
		response = userService.getStudentLoginInfo(request2.getUserName(), request2.getPassword());
		log.info(response.getErrMsg());
		assertFalse(response.isSuccess());
	}

	@Test
	public void testTeacherLogin() {
		RequestRegister request = mockRegisterRequest();
		log.info("register with userName: " + request.getUserName() +
				"with pwd: " + request.getPassword());
		userService.registerForTeacher(request.getUserName(), request.getPassword());

		ResponseRegister response = userService.getTeacherLoginInfo(request.getUserName(), request.getPassword());
		assertTrue(response.isSuccess());

		// Test invalid password
		RequestRegister request1 = mockRegisterRequest();
		request1.setUserName(request.getUserName());
		response = userService.getTeacherLoginInfo(request1.getUserName(), request1.getPassword());
		log.info(response.getErrMsg());
		assertFalse(response.isSuccess());

		// Test invalid username
		RequestRegister request2 = mockRegisterRequest();
		request2.setPassword(request.getPassword());
		response = userService.getTeacherLoginInfo(request2.getUserName(), request2.getPassword());
		log.info(response.getErrMsg());
		assertFalse(response.isSuccess());
	}

	@Test
	public void testUpdateStudentInfo() {
		Student student = UserDAOTest.mockStudent();
		if(!studentDAO.create(student)){
			log.error("无法插入学生，测试无法进行");
			fail();
		}
		// 更新手机号
		student.setPhone(StringUtil.generatePhoneNumbers());
		RequestUpdateStudent request = getRequestUpdateStudent(student);
		BaseResponse response = userService.updateStudentInfo(request);
		Assert.assertTrue(response.isSuccess());

		// 测试数据库中更新后的student与该student信息是否一致
		Student queriedStudent = studentDAO.queryBySessionKey(student.getSessionKey());
		Assert.assertNotNull(queriedStudent);
		Assert.assertEquals(student.getPhone(), queriedStudent.getPhone());
		// 没有更新的信息不应发生变动
		Assert.assertEquals(queriedStudent.getEmail(), student.getEmail());
		Assert.assertEquals(queriedStudent.getUserName(), student.getUserName());
		Assert.assertEquals(queriedStudent.getRealName(), student.getRealName());

		request.setSessionKey(RandomStringUtils.randomAlphanumeric(10));
		response = userService.updateStudentInfo(request);
		Assert.assertFalse(response.isSuccess());
	}

	@Test
	public void testGetStudentInfo() {
		Student student = UserDAOTest.mockStudent();
		if(!studentDAO.create(student)){
			log.error("无法插入学生，测试无法进行");
			fail();
		}
		ResponseGetStudent response = userService.getStudentInfo(student.getSessionKey());
		Assert.assertNotNull(response);
		Assert.assertTrue(response.isSuccess());
		Assert.assertEquals(response.getUserName(), student.getUserName());

		response = userService.getStudentInfo(StringUtil.generateRandomString("invalid"));
		Assert.assertFalse(response.isSuccess());
	}

	@Test
	public void testUpdateTeacherInfo() {
		Teacher teacher = UserDAOTest.mockTeacher();
		if(!teacherDAO.create(teacher)){
			log.error("无法插入老师，测试无法进行");
			fail();
		}
		// 更新老师手机号
		teacher.setPhone(StringUtil.generatePhoneNumbers());
		RequestUpdateTeacher request = getRequestUpdateTeacher(teacher);
		BaseResponse response = userService.updateTeacherInfo(request);
		Assert.assertTrue(response.isSuccess());

		// 判断数据库中更新后的的teacher与该teacher是否一致
		Teacher queriedTeacher = teacherDAO.queryBySessionKey(teacher.getSessionKey());
		Assert.assertNotNull(queriedTeacher);
		Assert.assertEquals(teacher.getPhone(), queriedTeacher.getPhone());
		// 没有更新的信息不应发生变化
		Assert.assertEquals(teacher.getEmail(), queriedTeacher.getEmail());
		Assert.assertEquals(teacher.getUserName(), queriedTeacher.getUserName());
		Assert.assertEquals(teacher.getRealName(), queriedTeacher.getRealName());

		request.setSessionKey(RandomStringUtils.randomAlphanumeric(10));
		response = userService.updateTeacherInfo(request);
		Assert.assertFalse(response.isSuccess());
	}

	@Test
	public void testGetTeacherInfo() {
		Teacher teacher = UserDAOTest.mockTeacher();
		if(!teacherDAO.create(teacher)){
			log.error("无法插入老师，测试无法进行");
			fail();
		}
		ResponseGetTeacher response = userService.getTeacherInfo(teacher.getSessionKey());
		Assert.assertNotNull(response);
		Assert.assertTrue(response.isSuccess());
		Assert.assertEquals(response.getUserName(), teacher.getUserName());

		response = userService.getTeacherInfo(StringUtil.generateRandomString("invalid"));
		Assert.assertFalse(response.isSuccess());
	}

	public static RequestUpdateStudent getRequestUpdateStudent(Student student) {
		RequestUpdateStudent request = new RequestUpdateStudent(student.getSessionKey(),
				student.getUserName(), student.getRealName(), student.getPhone(),
				student.getEmail(), student.getSex());
		return request;
	}

	public static RequestUpdateTeacher getRequestUpdateTeacher(Teacher teacher) {
		RequestUpdateTeacher request = new RequestUpdateTeacher(teacher.getSessionKey(),
				teacher.getUserName(), teacher.getRealName(), teacher.getPhone(),
				teacher.getEmail(), teacher.getDescription());
		return request;
	}

	public static RequestRegister mockRegisterRequest() {
		RequestRegister request = new RequestRegister();
		request.setPassword(StringUtil.generateRandomString("testPassword"));
		request.setUserName(StringUtil.generateRandomString("testUserName"));
		return request;
	}
}
