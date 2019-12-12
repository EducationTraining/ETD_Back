package com.etd.etdservice.serivce;

import com.etd.etdservice.bean.users.response.ResponseRegister;

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
}

