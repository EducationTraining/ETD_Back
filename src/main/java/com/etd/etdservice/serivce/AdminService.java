package com.etd.etdservice.serivce;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseGetCoursesCategories;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseRegister;

public interface AdminService {
	/**
	 *
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
	ResponseGetCourses getAllCourses(String sessionKey);

	/**
	 *
	 * @param courseId
	 * @param status
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
	BaseResponse updateCourseStatus(Integer courseId, Integer status, String sessionKey);

	/**
	 *
	 * @param sessionKey 管理员sessionKey
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

	/**
	 * 修改课程类别
	 * @param categoryId
	 * @param categoryName
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
	BaseResponse updateCourseCategory(Integer categoryId, String categoryName, String sessionKey);

	/**
	 * 查询所有课程类别
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
	ResponseGetCoursesCategories getAllCoursesCategories(String sessionKey);

	/**
	 * 增加课程类别
	 * @param categoryId
	 * @param categoryName
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
    BaseResponse addCourseCategory(int categoryId, String categoryName, String sessionKey);

	/**
	 * 删除课程类别
	 * @param categoryId
	 * @param sessionKey 管理员sessionKey
	 * @return
	 */
	BaseResponse deleteCourseCategory(int categoryId, String sessionKey);

	/**
	 * 查询某一类的所有课程
	 * @param sessionKey
	 * @param categoryId
	 * @return
	 */
    ResponseGetCourses getCoursesByCategory(String sessionKey, Integer categoryId);
}
