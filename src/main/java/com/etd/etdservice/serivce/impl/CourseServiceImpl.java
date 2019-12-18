package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.CourseStudentDAO;
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

	private static ResponseGetCourses fromCourses(List<Course> courses){
		List<ResponseCourse> courseList = new ArrayList<>();
		if(courses == null || courses.size() == 0){
			return new ResponseGetCourses(false, "no selected courses!", courseList);
		}
		for(Course course : courses){
			Teacher teacher = teacherDAO.queryById(course.getTeacherId());
			ResponseGetTeacher responseGetTeacher = ResponseGetTeacher.fromBeanToResponse(teacher);
			Integer studentNum = courseStudentDAO.queryStudentNumbersByCourseId(course.getId());

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

	@Override
	public BaseResponse attendCourse(Integer courseId, String sessionKey) {
		return null;
	}

	@Override
	public BaseResponse withdrawCourse(Integer courseId, String sessionKey) {
		return null;
	}

	@Override
	public ResponseIsAttendCourse isAttendCourse(Integer courseId, String sessionKey) {
		return null;
	}

	@Override
	public ResponseGetCourses getAttendedCourses(String sessionKey) {
		return null;
	}

	@Override
	public ResponseGetStudent getAttendStudents(Integer courseId, String sessionKey) {
		return null;
	}

	@Override
	public BaseResponse remarkCourse(RequestRemarkCourse request) {
		return null;
	}
}
