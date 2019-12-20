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
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;

import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.CourseService;
import com.etd.etdservice.utils.FileHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
@Slf4j
public class CourseServiceImpl implements CourseService {



	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private CourseDAO courseDAO;

	private static TeacherDAO teacherDAO;

	private static CourseStudentDAO courseStudentDAO;

	@Value("${image_root_path}")
	private String imageRootPath;

	@Value("${url_starter}")
	private String urlStarter;

	public static final String COURSE_TYPE = "avatars";

	@Autowired
	public void setTeacherDAO(TeacherDAO teacherDAO){
		this.teacherDAO = teacherDAO;
	}

	@Autowired
	public void setCourseStudentDAO(CourseStudentDAO courseStudentDAO){
		this.courseStudentDAO = courseStudentDAO;
	}

	/**
	 * the number of hottest courses.
	 */
	public static final int HOTTEST_COURSE_NUMBER = 5;

	public static final int LATEST_COURSE_NUMBER = 5;

	private static ResponseGetCourses fromCourses(List<Course> courses) {
		List<ResponseCourse> courseList = new ArrayList<>();
		if(courses == null || courses.size() == 0) {
			return new ResponseGetCourses(false, "no selected courses!", courseList);
		}
		for(Course course : courses) {
			Teacher teacher = teacherDAO.queryById(course.getTeacherId());
			log.info("teacher: " + teacher + " course: " + course);

			ResponseGetTeacher responseGetTeacher = ResponseGetTeacher.fromBeanToResponse(teacher);
			Integer studentNum = courseStudentDAO.getStudentCountsByCourseId(course.getId());

			ResponseCourse responseCourse = ResponseCourse.fromBeanToResponse(course, responseGetTeacher, studentNum);
			courseList.add(responseCourse);
		}
		return new ResponseGetCourses(true, "", courseList);
	}


	@Override
	public ResponseGetCourses getHottestCourses() {
		List<Course> courses = courseDAO.queryHottestCourses(HOTTEST_COURSE_NUMBER);
		return fromCourses(courses);
	}

	@Override
	public ResponseGetCourses getLatestCourses() {
		List<Course> courses = courseDAO.queryLatestCourses(LATEST_COURSE_NUMBER);
		return fromCourses(courses);
	}

	@Override
	public ResponseGetCourses getCourses(int page, int limit) {
		PageHelper.startPage(page, limit);
		Page<Course> courses = courseDAO.queryAllCourses();
		return fromCourses(courses);
	}

	@Override
	public ResponseUploadAvatar uploadCoursePic(MultipartFile file, Integer courseId, String sessionKey) {
		if(file == null || courseId == null || sessionKey == null){
			return new ResponseUploadAvatar(false, "param error", null);
		}
		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		if(teacher == null){
			return new ResponseUploadAvatar(false, "invalid sessionKey", null);
		}
		Course course = courseDAO.queryById(courseId);
		if(course == null){
			return new ResponseUploadAvatar(false, "invalid courseId", null);
		}
		if(course.getTeacherId() != teacher.getId()){
			return new ResponseUploadAvatar(false, "have no permission to update course", null);
		}
		String avatarUrl = null;
		try{
			avatarUrl = FileHelper.uploadPic(file, imageRootPath, COURSE_TYPE, urlStarter);
		}catch (Exception e){
			log.warn("课程图片上传失败！\n" + e.getMessage());
			return new ResponseUploadAvatar(false, "upload course picture failed.\n" + e.getMessage(), null);
		}
		course.setAvatarUrl(avatarUrl);
		if(courseDAO.update(course)){
			log.info("teacher (id: " + teacher.getId() + ") update course (id: " + course.getId() + ") successfully.");
			return new ResponseUploadAvatar(true, "", avatarUrl);
		}else{
			log.warn("teacher (id: " + teacher.getId() + ") try to update course(id: " + course.getId() + ") but fail!");
			return new ResponseUploadAvatar(false, "update course failed", null);
		}
	}

	@Override
	public BaseResponse updateCourseInfo(RequestUpdateCourse request) {
		String sessionKey = request.getSessionKey();
		if(sessionKey == null){
			return new BaseResponse(false, "param error");
		}
		Course course = courseDAO.queryById(request.getCourseId());
		if(course == null){
			return new BaseResponse(false, "invalid courseId");
		}
		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		if(teacher == null){
			return new BaseResponse(false, "invalid sessionKey");
		}
		if(teacher.getId() != course.getTeacherId()){
			return new BaseResponse(false, "have no permission to update courseInfo");
		}
		course.setName(request.getName());
		course.setPages(request.getPages());
		if(request.getStartTime() != null){
			course.setStartTime(new Date(request.getStartTime()));
		}else {
			course.setStartTime(null);
		}
		course.setWeeks(request.getWeeks());
		course.setStatus(request.getStatus());
		course.setDescription(request.getDescription());
		course.setNote(request.getNote());
		course.setAvatarUrl(request.getAvatarUrl());
		if(courseDAO.update(course)){
			log.info("teacher (id: " + teacher.getId() + ") update course (id: " + course.getId() + ") successfully.");
			return new BaseResponse(true, "courseInfo update");
		}else{
			log.warn("teacher (id: " + teacher.getId() + ") try to update course(id: " + course.getId() + ") but fail!");
			return new BaseResponse(false, "update courseInfo failed");
		}
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
		if (teacherId == course.getTeacherId()) {
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
