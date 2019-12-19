package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.requests.RequestUpdateStudent;
import com.etd.etdservice.bean.users.requests.RequestUpdateTeacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.UserService;
import com.etd.etdservice.utils.FileHelper;
import com.etd.etdservice.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private TeacherDAO teacherDAO;

	@Value("${image_root_path}")
	private String imageRootPath;

	@Value("${url_starter}")
	private String urlStarter;

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
		String sessionKey = MD5Util.getMD5String(userName + password);
		registerStudent.setSessionKey(sessionKey);
		registerStudent.setUserName(userName);
		registerStudent.setCreateTime(new Date());
		if (!studentDAO.create(registerStudent)) {
			return new ResponseRegister(false,
					"cannot create student", null);
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
			return new ResponseRegister(false,
					"invalid userName", null);
		}

		Teacher registerTeacher = new Teacher();
		String sessionKey = MD5Util.getMD5String(userName + password);
		registerTeacher.setSessionKey(sessionKey);
		registerTeacher.setUserName(userName);
		registerTeacher.setCreateTime(new Date());
		if (!teacherDAO.create(registerTeacher)) {
			return new ResponseRegister(false,
					"cannot create teacher", null);
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
			if (resSessionKey.equals(MD5Util.getMD5String(userName + password))) {
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
			if (resSessionKey.equals(MD5Util.getMD5String(userName + password))) {
				return new ResponseRegister(true, "",  resSessionKey);
			} else {
				return new ResponseRegister(false,
						"invalid password", null);
			}
		}
	}

	@Override
	public BaseResponse updateStudentInfo(RequestUpdateStudent request) {
		if (request.getSessionKey() == null) {
			return new BaseResponse(false, "no sessionKey!");
		}
		Student student = studentDAO.queryBySessionKey(request.getSessionKey());
		if (student == null) {
			return new BaseResponse(false, "invalid sessionKey!");
		}

		student.setUserName(request.getUserName());
		student.setRealName(request.getRealName());
		student.setPhone(request.getPhone());
		student.setEmail(request.getEmail());
		student.setSex(request.getSex());

		if (studentDAO.update(student)) {
			return new BaseResponse(true, "");
		}
		log.warn("can not update studentInfo, studentId (" + student.getId() + ")");
		return new BaseResponse(false, "update student information error");
	}

	@Override
	public ResponseGetStudent getStudentInfo(String sessionKey) {
		if (sessionKey == null) {
			return null;
		}
		return null;
	}

	@Override
	public BaseResponse updateTeacherInfo(RequestUpdateTeacher request) {
		if (request.getSessionKey() == null) {
			return new BaseResponse(false, "no sessionKey!");
		}
		Teacher teacher = teacherDAO.queryBySessionKey(request.getSessionKey());
		if(teacher == null) {
			return new BaseResponse(false, "invalid sessionKey!");
		}

		teacher.setUserName(request.getUserName());
		teacher.setRealName(request.getRealName());
		teacher.setPhone(request.getPhone());
		teacher.setEmail(request.getEmail());
		teacher.setDescription(request.getDescription());

		if(teacherDAO.update(teacher)) {
			return new BaseResponse(true, "");
		}
		log.warn("can not update studentInfo, studentId (" + teacher.getId() + ")");
		return new BaseResponse(false, "update student information error");
	}

	@Override
	public ResponseGetTeacher getTeacherInfo(String sessionKey) {
		return null;
	}

	@Override
	public ResponseUploadAvatar uploadStudentAvatar(MultipartFile file, String sessionKey) {
		if (file == null || sessionKey == null) {
			return new ResponseUploadAvatar(false, "param error", null);
		}

		Student student = studentDAO.queryBySessionKey(sessionKey);
		if (student == null) {
			return new ResponseUploadAvatar(false, "invalid sessionKey", null);
		}

		try {
			String avatarUrl = FileHelper.uploadPic(file, imageRootPath, "avatars", urlStarter);
			student.setAvatarUrl(avatarUrl);
			if (studentDAO.update(student)) {
				return new ResponseUploadAvatar(true, "", avatarUrl);
			} else {
				return new ResponseUploadAvatar(false, "update student failed", null);
			}
		} catch (Exception e) {
			return new ResponseUploadAvatar(false, e.getMessage(), null);
		}
	}

	@Override
	public ResponseUploadAvatar uploadTeacherAvatar(MultipartFile file, String sessionKey) {
		if (file == null || sessionKey == null) {
			return new ResponseUploadAvatar(false, "param error", null);
		}

		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		if (teacher == null) {
			return new ResponseUploadAvatar(false, "invalid sessionKey", null);
		}

		try {
			String avatarUrl = FileHelper.uploadPic(file, imageRootPath, "avatars", urlStarter);
			teacher.setAvatarUrl(avatarUrl);
			log.info(teacher.toString());
			if (teacherDAO.update(teacher)) {
				return new ResponseUploadAvatar(true, "", avatarUrl);
			} else {
				return new ResponseUploadAvatar(false, "update teacher failed", null);
			}
		} catch (Exception e) {
			return new ResponseUploadAvatar(false, e.getMessage(), null);
		}
	}
}
