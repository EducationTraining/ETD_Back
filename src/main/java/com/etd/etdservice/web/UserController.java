package com.etd.etdservice.web;

import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.serivce.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
	public ResponseUploadAvatar uploadAvatarForTeacher(@RequestParam("file") MultipartFile file, @RequestParam("sessionKey") String sessionKey) {
		return service.uploadTeacherAvatar(file, sessionKey);
	}

	@RequestMapping(value = "/upload-avatar/student", method = RequestMethod.POST)
	public ResponseUploadAvatar uploadAvatarForStudent(@RequestParam("file") MultipartFile file, @RequestParam("sessionKey") String sessionKey) {
		return service.uploadStudentAvatar(file, sessionKey);
	}
}
