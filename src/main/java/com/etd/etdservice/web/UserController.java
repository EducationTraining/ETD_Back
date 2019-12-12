package com.etd.etdservice.web;

import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.serivce.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Request;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService service;

	@RequestMapping(value = "/register/student", method = RequestMethod.POST)
	public ResponseRegister registerForStudent(@RequestBody RequestRegister request) {
		String password = request.getPassword();
		String userName = request.getUserName();
		return service.registerForStudent(userName, password);
	}

	@RequestMapping(value = "/register/teacher", method = RequestMethod.POST)
	public  ResponseRegister registerForTeacher(@RequestBody RequestRegister request) {
		String password = request.getPassword();
		String userName = request.getUserName();
		return service.registerForTeacher(userName, password);
	}

	@RequestMapping(value = "/login/student", method = RequestMethod.POST)
	public ResponseRegister getStudentLoginInfo(@RequestBody RequestRegister request) {
		String password = request.getPassword();
		String userName = request.getUserName();
		return service.getStudentLoginInfo(userName, password);
	}

	@RequestMapping(value = "/login/teacher", method = RequestMethod.POST)
	public ResponseRegister getTeacherLoginInfo(@RequestBody RequestRegister request) {
		String password = request.getPassword();
		String userName = request.getUserName();
		return service.getTeacherLoginInfo(userName, password);
	}
}
