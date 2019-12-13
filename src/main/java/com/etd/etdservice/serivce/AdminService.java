package com.etd.etdservice.serivce;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseRegister;

public interface AdminService {
	/**
	 *
	 * @param sessionKey
	 * @return
	 */
	ResponseGetCourses getAllCourses(String sessionKey);

	/**
	 *
	 * @param courseId
	 * @param status
	 * @param sessionKey
	 * @return
	 */
	BaseResponse updateCourseStatus(Integer courseId, Integer status, String sessionKey);

	/**
	 *
	 * @param sessionKey
	 * @return
	 */
	ResponseGetAdmin getAdminInfo(String sessionKey);

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	ResponseRegister register(String userName, String password);

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	ResponseRegister getLoginInfo(String userName, String password);
}
