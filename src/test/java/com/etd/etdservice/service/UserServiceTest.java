package com.etd.etdservice.service;

import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.UserDAOTest;
import com.etd.etdservice.serivce.UserService;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
	@Autowired
	private UserService userService;

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

	public static RequestRegister mockRegisterRequest() {
		RequestRegister request = new RequestRegister();
		request.setPassword(StringUtil.generateRandomString("testPassword"));
		request.setUserName(StringUtil.generateRandomString("testUserName"));
		return request;
	}
}
