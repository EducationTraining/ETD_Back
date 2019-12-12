package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.UserService;
import com.etd.etdservice.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private TeacherDAO teacherDAO;

	@Override
	public ResponseRegister registerForStudent(String userName, String password) {
		if (userName == null || password == null) {
			return new ResponseRegister(false, "param error", null);
		}

		Student resStudent = studentDAO.queryByUserName(userName);
		if (resStudent != null) {
			return new ResponseRegister(false, "invalid userName", null);
		}

		Student registerStudent = new Student();
		String sessionKey = MD5Util.getMD5String(password);
		registerStudent.setSessionKey(sessionKey);
		registerStudent.setUserName(userName);
		registerStudent.setCreateTime(new Date());
		if (!studentDAO.create(registerStudent)) {
			return new ResponseRegister(false, "cannot create student", null);
		}

		return new ResponseRegister(true, "", sessionKey);
	}

	@Override
	public ResponseRegister registerForTeacher(String userName, String password) {
		if (userName == null || password == null) {
			return new ResponseRegister(false, "param error", null);
		}

		Teacher resTeacher = teacherDAO.queryByUserName(userName);
		if (resTeacher != null) {
			return new ResponseRegister(false, "invalid userName", null);
		}

		Teacher registerTeacher = new Teacher();
		String sessionKey = MD5Util.getMD5String(password);
		registerTeacher.setSessionKey(sessionKey);
		registerTeacher.setUserName(userName);
		registerTeacher.setCreateTime(new Date());
		if (!teacherDAO.create(registerTeacher)) {
			return new ResponseRegister(false, "cannot create teacher", null);
		}

		return new ResponseRegister(true, "", sessionKey);
	}

	@Override
	public ResponseRegister getStudentLoginInfo(String userName, String password) {
		if (userName == null || password == null) {
			return new ResponseRegister(false, "param error", null);
		}

		Student resStudent = studentDAO.queryByUserName(userName);
		if (resStudent == null) {
			return new ResponseRegister(false,
				 	"invalid user name, may need register", null);
		} else {
			String resSessionKey = resStudent.getSessionKey();
			if (resSessionKey.equals(MD5Util.getMD5String(password))) {
				return new ResponseRegister(true, "",  resSessionKey);
			} else {
				return new ResponseRegister(false,
						"invalid password", null);
			}
		}
	}

	@Override
	public ResponseRegister getTeacherLoginInfo(String userName, String password) {
		if (userName == null || password == null) {
			return new ResponseRegister(false, "param error", null);
		}

		Teacher resTeacher = teacherDAO.queryByUserName(userName);
		if (resTeacher== null) {
			return new ResponseRegister(false,
					"invalid user name, may need register", null);
		} else {
			String resSessionKey = resTeacher.getSessionKey();
			if (resSessionKey.equals(MD5Util.getMD5String(password))) {
				return new ResponseRegister(true, "",  resSessionKey);
			} else {
				return new ResponseRegister(false,
						"invalid password", null);
			}
		}
	}
}
