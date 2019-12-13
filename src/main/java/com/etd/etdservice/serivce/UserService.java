package com.etd.etdservice.serivce;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.users.requests.RequestUpdateStudent;
import com.etd.etdservice.bean.users.requests.RequestUpdateTeacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	/**
	 * 学生用户注册
	 * @param userName 用户名
	 * @param password 密码
	 * @return a ResponseRegister Object contains sessionKey.
	 */
	ResponseRegister registerForStudent(String userName, String password);

	/**
	 * 老师用户注册
	 * @param userName 用户名
	 * @param password 密码
	 * @return a ResponseRegister Object contains sessionKey.
	 */
	ResponseRegister registerForTeacher(String userName, String password);

	/**
	 * 获取学生用户sessionKey
	 * @param userName 用户名
	 * @param password 密码
	 * @return a ResponseRegister Object contains sessionKey.
	 */
	ResponseRegister getStudentLoginInfo(String userName, String password);

	/**
	 * 获取老师用户sessionKey
	 * @param userName 用户名
	 * @param password 密码
	 * @return a ResponseRegister Object contains sessionKey.
	 */
	ResponseRegister getTeacherLoginInfo(String userName, String password);

	/**
	 * 更新学生信息
	 * @param request a RequestUpdateStudent Object
	 * @return a BaseResponse Object
	 */
	BaseResponse updateStudentInfo(RequestUpdateStudent request);

	/**
	 * 更新老师信息
	 * @param request a RequestUpdateTeacher Object
	 * @return a BaseResponse Object
	 */
	BaseResponse updateTeacherInfo(RequestUpdateTeacher request);

	/**
	 * 上传学生头像
	 * @param file 用户头像文件
	 * @param sessionKey 用户sessionKey
	 * @return a ResponseUploadAvatar Object
	 */
	ResponseUploadAvatar uploadStudentAvatar(MultipartFile file, String sessionKey);

	/**
	 * 上传老师头像
	 * @param file 用户头像文件
	 * @param sessionKey 用户sessionKey
	 * @return a ResponseUploadAvatar Object
	 */
	ResponseUploadAvatar uploadTeacherAvatar(MultipartFile file, String sessionKey);
}

