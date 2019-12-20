package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.*;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.CourseStudentDAO;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseStudentDAO courseStudentDAO;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private TeacherDAO teacherDAO;

	@Autowired
	private CourseDAO courseDAO;

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
		if (sessionKey == null) {
			return new ResponseGetCourses(false, "param error",null);
		}
		// 根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		// 根据studentId查已选课程id
		List<Integer> courseIdList = courseStudentDAO.queryCourseIdByStudentId(studentId);

		List<ResponseCourse> responseCourses = new ArrayList<>();

		for (Integer courseId : courseIdList) {
			Course course = courseDAO.queryById(courseId);
			Teacher teacher = courseDAO.queryTeacherByCourseId(courseId);
			ResponseCourse rC = new ResponseCourse();
			ResponseCourse responseCourse = rC.fromBeanToResponse(course, ResponseGetTeacher.fromBeanToResponse(teacher), studentId);
			responseCourses.add(responseCourse);
		}

		ResponseGetCourses attendedCourses = new ResponseGetCourses(true,"",responseCourses);

		return attendedCourses;

	}

	/**
	 * 获取某门课参加的学生
	 * @param courseId 课程号
	 * @param sessionKey 老师sessionKey
	 * @return
	 */
	@Override
	public ResponseGetStudents getAttendStudents(Integer courseId, String sessionKey) {
		if (courseId == null || sessionKey == null) {
			return new ResponseGetStudents(false,"param error",null);
		}
		// 判断该老师是否是该课程的任课老师
		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		int teacherId = teacher.getId();
		Course course = courseDAO.queryById(courseId);
		if (teacherId == course.getTeacherId()){
			// 如果是，根据courseId查询选课学生
			List<Student> attendStudents = courseStudentDAO.getAttendStudents(courseId);
			 List<ResponseGetStudent> studentsList = new ArrayList<>();
			for (Student attendStudent : attendStudents) {
				ResponseGetStudent responseGetStudent = ResponseGetStudent.fromBeanToResponse(attendStudent);
				studentsList.add(responseGetStudent);
			}
			return new ResponseGetStudents(true,"",studentsList);
		} else {
			// 如果不是，返回课程号错误
			return new ResponseGetStudents(false,"courseId error",null);
		}
	}


	/**
	 * 对某门课进行评价
	 * @param request
	 * @return BaseResponse
	 */
	@Override
	public BaseResponse remarkCourse(RequestRemarkCourse request) {
		Integer courseId = request.getCourseId();  // 课程ID
		String sessionKey = request.getSessionKey(); // 学生sessionKey
		String remark = request.getRemark(); // 课程评价
		Double score = request.getScore();  // 课程评分
		// 根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		// 判断该学生是否选择了待评价课程（是否有权限评价）
		CourseStudent attendCourse = courseStudentDAO.isAttendCourse(courseId, studentId);

		if (null != attendCourse) {
			CourseStudentRemark courseStudentRemark = new CourseStudentRemark(0, courseId, studentId, remark, score);

			boolean status = courseStudentDAO.remarkCourse(courseStudentRemark);

			if (status == true) {
				// 评价成功
				return new BaseResponse(true,"");
			} else {
				// 评价失败
				return new BaseResponse(false,"remarkCourse failed");
			}
		} else {
			return new BaseResponse(false,"not attend the course");
		}

	}
}
