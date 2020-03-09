package com.etd.etdservice.serivce;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.CourseNote;
import com.etd.etdservice.bean.course.request.*;
import com.etd.etdservice.bean.course.response.*;
import com.etd.etdservice.bean.users.response.ResponseGetStudents;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
	/**
	 * 返回5门最热的课程（按照score排序）
	 * @return
	 */
	ResponseGetCourses getHottestCourses();

	/**
	 * 返回5门最新的课程（按createTime排序）
	 * @return
	 */
	ResponseGetCourses getLatestCourses();

	/**
	 * 获取某一页的课程
	 * @param page
	 * @param limit
	 * @return
	 */
	ResponseGetCourses getCourses(int page, int limit);

	/**
	 * 上传课程图片（实现可仿上传用户头像）
	 * @param file
	 * @param courseId
	 * @param sessionKey 老师sessionKey
	 * @return
	 */
	ResponseUploadAvatar uploadCoursePic(MultipartFile file, Integer courseId, String sessionKey);

	/**
	 * 更新课程信息 注意对startTime的处理
	 * @param request a RequestUpdateCourse Object
	 * @return
	 */
	BaseResponse updateCourseInfo(RequestUpdateCourse request);

	/**
	 * 更新课程信息 注意对startTime的处理
	 * @param request a RequestUploadCourse Object
	 * @return
	 */
	ResponseUploadCourse uploadCourseInfo(RequestUploadCourse request);

	/**
	 * 选课
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	BaseResponse attendCourse(Integer courseId, String sessionKey);

	/**
	 * 退课
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	BaseResponse withdrawCourse(Integer courseId, String sessionKey);

	/**
	 * 获取某学生是否参加了某门课程
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	ResponseIsAttendCourse isAttendCourse(Integer courseId, String sessionKey);

	/**
	 * 获取某学生参加了的课程
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	ResponseGetCourses getAttendedCourses(String sessionKey);

	/**
	 * 获取某门课参加的学生
	 * @param courseId
	 * @param sessionKey 老师sessionKey
	 * @return
	 */
	ResponseGetStudents getAttendStudents(Integer courseId, String sessionKey);

	/**
	 * 获取某门课程
	 * @param courseId
	 * @return
	 */
	ResponseCourse getCourse(Integer courseId);

	/**
	 * 对某门课进行评价
	 * @param request
	 * @return
	 */
	BaseResponse remarkCourse(RequestRemarkCourse request);

	/**
	 * 为某门子课程上传材料
	 * @param video 视频文件
	 * @param subcourseId 子课程id
	 * @param sessionKey 老师sessionKey
	 * @return a ResponseUploadMaterial contains courseMaterial
	 */
	ResponseUploadMaterial uploadSubcourseMaterial(MultipartFile video, Integer subcourseId, String sessionKey);

	/**
	 *
	 * @param pages 课程目录json字符串 里面不包含subcourse id
	 * @param courseId 课程id
	 * @param sessionKey 老师sessionKey
	 * @return
	 */
	ResponseUpdateCoursePages updateCoursePages(String pages, Integer courseId, String sessionKey);

	/**
	 * 发布课程公告
	 * @param request
	 * @return
	 */
	BaseResponse publishCourseNote(RequestPublishCourseNote request);

	/**
	 * 更新课程公告
	 * @param request
	 * @return
	 */
	BaseResponse updateCourseNote(RequestUpdateCourseNote request);

	/**
	 * 返回课程的所有符合特定类型的公告
	 * @param courseId
	 * @param type
	 * @return
	 */
	ResponseCourseNotes getNotesWithSpecificType(Integer courseId, Integer type);

	/**
	 * 返回该课程的所有公告
	 * @param courseId
	 * @return
	 */
	ResponseCourseNotes getNotes(Integer courseId);
}
