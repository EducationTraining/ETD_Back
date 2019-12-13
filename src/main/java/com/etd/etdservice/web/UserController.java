package com.etd.etdservice.web;

import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.requests.RequestUploadAvatar;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.serivce.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	@RequestMapping(value = "/upload-avatar/teacher", method = RequestMethod.POST)
	public ResponseUploadAvatar uploadAvatarForTeacher(@RequestBody RequestUploadAvatar request) {
		MultipartFile file = request.getFile();
		String sessionKey = request.getSessionKey();
		return service.uploadTeacherAvatar(file, sessionKey);
	}

	@RequestMapping(value = "/upload-avatar/student", method = RequestMethod.POST)
	public ResponseUploadAvatar uploadAvatarForStudent(@RequestBody RequestUploadAvatar request) {
		MultipartFile file = request.getFile();
		String sessionKey = request.getSessionKey();
		return service.uploadStudentAvatar(file, sessionKey);
	}
}
