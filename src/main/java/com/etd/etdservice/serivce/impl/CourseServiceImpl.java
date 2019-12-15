package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.CourseStudentDAO;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.serivce.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseStudentDAO courseStudentDAO;

	@Autowired
	private StudentDAO studentDAO;

	@Override
	public ResponseGetCourses getHottestCourses() {
		return null;
	}

	@Override
	public ResponseGetCourses getLatestCourses() {
		return null;
	}

	@Override
	public ResponseGetCourses getCourses(int page, int limit) {
		return null;
	}

	@Override
	public ResponseUploadAvatar uploadCoursePic(MultipartFile file, Integer courseId, String sessionKey) {
		return null;
	}

	@Override
	public BaseResponse updateCourseInfo(RequestUpdateCourse request) {
		return null;
	}


	/**
	 * 选课
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	@Override
	public BaseResponse attendCourse(Integer courseId, String sessionKey) {
		if (courseId == null || sessionKey == null) {
			return new BaseResponse(false, "param error");
		}
		//根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();
		Date createDate = new Date();

		CourseStudent courseStudent = new CourseStudent(0, courseId, studentId, createDate);

		boolean status = courseStudentDAO.attendCourse(courseStudent);
		if(status == true){
			//选课成功
			return new BaseResponse(true,"");
		}else{
			//选课失败
			return new BaseResponse(false,"attendCourse failed");
		}

	}

	/**
	 * 退课
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	@Override
	public BaseResponse withdrawCourse(Integer courseId, String sessionKey) {
		if (courseId == null || sessionKey == null) {
			return new BaseResponse(false, "param error");
		}
		//根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		boolean status = courseStudentDAO.withdrawCourse(courseId,studentId);
		if(status == true){
			//退课成功
			return new BaseResponse(true,"");
		}else{
			//退课失败
			return new BaseResponse(false,"withdrawCourse failed");
		}

	}

	/**
	 * 获取某学生是否参加了某门课程
	 * @param courseId
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	@Override
	public ResponseIsAttendCourse isAttendCourse(Integer courseId, String sessionKey) {
		if (courseId == null || sessionKey == null) {
			return new ResponseIsAttendCourse(false, "param error",false);
		}

		//根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		CourseStudent courseStudent = courseStudentDAO.isAttendCourse(courseId, studentId);
		if(courseStudent !=null){
			//参加该课
			return new ResponseIsAttendCourse(true,"",true);
		}else{
			//未参加该课
			return new ResponseIsAttendCourse(true,"",false);
		}

	}

	/**
	 * 获取某学生参加了的课程
	 * @param sessionKey 学生sessionKey
	 * @return
	 */
	@Override
	public ResponseGetCourses getAttendedCourses(String sessionKey) {
		return null;
	}

	/**
	 * 获取某门课参加的学生
	 * @param courseId
	 * @param sessionKey 老师sessionKey
	 * @return
	 */
	@Override
	public ResponseGetStudent getAttendStudents(Integer courseId, String sessionKey) {
		return null;
	}


	/**
	 * 对某门课进行评价
	 * @param request
	 * @return
	 */
	@Override
	public BaseResponse remarkCourse(RequestRemarkCourse request) {
		return null;
	}
}
