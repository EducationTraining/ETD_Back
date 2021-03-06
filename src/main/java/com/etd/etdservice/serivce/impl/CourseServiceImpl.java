package com.etd.etdservice.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.CourseMaterial;
import com.etd.etdservice.bean.course.Subcourse;
import com.etd.etdservice.bean.course.SubcourseToMaterial;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.request.RequestUploadCourse;
import com.etd.etdservice.bean.course.response.*;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseGetStudents;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.*;
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

	@Autowired
	private CourseMaterialDAO courseMaterialDAO;

	@Autowired
	private SubcourseToMaterialDAO subcourseToMaterialDAO;

	@Autowired
	private SubcourseDAO subcourseDAO;

	private static TeacherDAO teacherDAO;

	private static CourseStudentDAO courseStudentDAO;

	@Value("${image_root_path}")
	private String imageRootPath;

	@Value("${video_root_path}")
	private String videoRootPath;

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
	private static final int HOTTEST_COURSE_NUMBER = 5;

	private static final int LATEST_COURSE_NUMBER = 5;

	private ResponseGetCourses fromCourses(List<Course> courses) {
		List<ResponseCourse> courseList = new ArrayList<>();
		if(courses == null || courses.size() == 0) {
			return new ResponseGetCourses(false, "no selected courses!", courseList);
		}
		for(Course course : courses) {
			Teacher teacher = teacherDAO.queryById(course.getTeacherId());
			log.info("teacher: " + teacher + " course: " + course);

			ResponseGetTeacher responseGetTeacher = ResponseGetTeacher.fromBeanToResponse(teacher);
			Integer studentNum = courseStudentDAO.getStudentCountsByCourseId(course.getId());
			String processedPages = processOriginalPages(course.getPages());
			ResponseCourse responseCourse = ResponseCourse.fromBeanToResponse(course, responseGetTeacher, studentNum, processedPages);
			courseList.add(responseCourse);
		}
		return new ResponseGetCourses(true, "", courseList);
	}

	/**
	 * 将只含id subcourse目录json字符串处理为含完整信息的目录json字符串
	 * @param pages
	 * @return
	 */
	private String processOriginalPages(String pages) {
		if (pages == null || pages.equals("")) {
			return pages;
		}
		try {
			JSONArray subcourseArray = JSON.parseArray(pages);
			// 遍历每一个一级子课程
			for (int i = 0; i < subcourseArray.size(); i++) {
				JSONObject firstSubcourseObj = subcourseArray.getJSONObject(i);
				// 为每个一级子课程查询子课程信息
				Integer id = firstSubcourseObj.getInteger("id");
				Subcourse firstSubcourse = subcourseDAO.queryById(id);
				firstSubcourseObj.put("title", firstSubcourse.getTitle());
				// 遍历二级子课程
				JSONArray secondSubcourses = firstSubcourseObj.getJSONArray("subcourses");
				for (int j = 0; j < secondSubcourses.size(); j++) {
					// 为每个二级子课程查询子课程信息
					JSONObject secondSubcourseObj = secondSubcourses.getJSONObject(j);
					Integer secondId = secondSubcourseObj.getInteger("id");
					Subcourse secondSubcourse = subcourseDAO.queryById(secondId);
					secondSubcourseObj.put("title", secondSubcourse.getTitle());
				}
			}
			return JSON.toJSONString(subcourseArray);
		} catch (Exception e) {
			log.info(e.getMessage());
			return pages;
		}
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
	public ResponseUploadCourse uploadCourseInfo(RequestUploadCourse request) {
		String sessionKey = request.getSessionKey();
		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		if (teacher == null) {
			return new ResponseUploadCourse(false, "invalid sessionKey", null);
		}
		Course course = RequestUploadCourse.fromRequestToBean(request, teacher.getId());
		if (!courseDAO.create(course)) {
			return new ResponseUploadCourse(false, "invalid sessionKey", null);
		}
		return new ResponseUploadCourse(true, "", course.getId());
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
		if(status){
			//选课成功
			return new BaseResponse(true, "");
		}else{
			//选课失败
			return new BaseResponse(false, "attendCourse failed");
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

		boolean status = courseStudentDAO.withdrawCourse(courseId, studentId);
		if(status){
			//退课成功
			return new BaseResponse(true, "");
		}else{
			//退课失败
			return new BaseResponse(false, "withdrawCourse failed");
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
			return new ResponseIsAttendCourse(false, "param error", false);
		}

		// 根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		CourseStudent courseStudent = courseStudentDAO.isAttendCourse(courseId, studentId);
		if(courseStudent != null){
			// 参加该课
			return new ResponseIsAttendCourse(true, "", true);
		}else{
			// 未参加该课
			return new ResponseIsAttendCourse(true, "", false);
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
			return new ResponseGetCourses(false, "param error", null);
		}
		// 根据学生sessionKey获取学生id
		Student student = studentDAO.queryBySessionKey(sessionKey);
		Integer studentId = student.getId();

		// 根据studentId查已选课程
		List<Course> courseList = courseStudentDAO.getAttendedCourses(studentId);

		return fromCourses(courseList);

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
			return new ResponseGetStudents(false, "param error", null);
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
//				// 获取该学生最近两门课
//				List<Course> latestTwoCourses = studentDAO.queryLatestTwoCourses(attendStudent.getId());
//				List<ResponseCourse> latestTwoResponseCourses = new ArrayList<>();
//				for (Course courseC : latestTwoCourses) {
//					Teacher teacherT = teacherDAO.queryById(courseC.getTeacherId());
//					ResponseGetTeacher responseGetTeacher = ResponseGetTeacher.fromBeanToResponse(teacherT);
//					Integer studentNum = courseStudentDAO.getStudentCountsByCourseId(courseC.getId());
//					ResponseCourse responseCourse = ResponseCourse.fromBeanToResponse(courseC, responseGetTeacher, studentNum, null);
//					latestTwoResponseCourses.add(responseCourse);
//				}
//				ResponseGetStudent responseGetStudent = ResponseGetStudent.fromBeanToResponse(attendStudent, latestTwoResponseCourses);
				ResponseGetStudent responseGetStudent = ResponseGetStudent.fromBeanToResponse(attendStudent, null);
				studentsList.add(responseGetStudent);
			}
			return new ResponseGetStudents(true, "", studentsList);
		} else {
			// 如果不是，返回课程号错误
			return new ResponseGetStudents(false, "courseId error", null);
		}
	}

	@Override
	public ResponseCourse getCourse(Integer courseId) {
		if (courseId == null) {
			return new ResponseCourse();
		}
		Course course = courseDAO.queryById(courseId);
		Teacher teacher = teacherDAO.queryById(course.getTeacherId());
		if (teacher == null) {
			return new ResponseCourse();
		}
		String processedPages = processOriginalPages(course.getPages());
		return ResponseCourse.fromBeanToResponse(course, ResponseGetTeacher.fromBeanToResponse(teacher),
				courseStudentDAO.getStudentCountsByCourseId(courseId), processedPages);
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

			if (status) {
				// 评价成功
				return new BaseResponse(true, "");
			} else {
				// 评价失败
				return new BaseResponse(false,"remarkCourse failed");
			}
		} else {
			return new BaseResponse(false,"not attend the course");
		}

	}

	@Override
	public ResponseUploadMaterial uploadSubcourseMaterial(MultipartFile video, Integer subcourseId, String sessionKey) {
		if (video == null || subcourseId == null || sessionKey == null) {
			return new ResponseUploadMaterial(false, "param error", new CourseMaterial());
		}
		if (isTeacherSessionKeyValid(sessionKey)) {
			return new ResponseUploadMaterial(false, "invalid sessionKey", new CourseMaterial());
		}
		if (subcourseDAO.queryById(subcourseId) == null) {
			return new ResponseUploadMaterial(false, "invalid subcourseId", new CourseMaterial());
		}
		try {
			String videoUrl = FileHelper.uploadVideo(video, videoRootPath, subcourseId, urlStarter);
			CourseMaterial courseMaterial = new CourseMaterial();
			courseMaterial.setVideoUrl(videoUrl);
			if (!courseMaterialDAO.create(courseMaterial)) {
				return new ResponseUploadMaterial(false, "Unable to update material to database", new CourseMaterial());
			}
			int materialId = courseMaterial.getId();
			SubcourseToMaterial subcourseToMaterial = new SubcourseToMaterial();
			subcourseToMaterial.setMaterialId(materialId);
			subcourseToMaterial.setSubcourseId(subcourseId);
			if (!subcourseToMaterialDAO.create(subcourseToMaterial)) {
				return new ResponseUploadMaterial(false, "Unable to update subcourseToMaterial to database", new CourseMaterial());
			}
			return new ResponseUploadMaterial(true, "", courseMaterial);
		} catch(Exception e) {
			return new ResponseUploadMaterial(false, e.getMessage(), new CourseMaterial());
		}
	}

	@Override
	public ResponseUpdateCoursePages updateCoursePages(String pages, Integer courseId, String sessionKey) {
		if (pages == null || sessionKey == null) {
			return new ResponseUpdateCoursePages(false, "param error", "");
		}
		if (isTeacherSessionKeyValid(sessionKey)) {
			return new ResponseUpdateCoursePages(false, "invalid sessionKey", "");
		}
		Course course = courseDAO.queryById(courseId);
		if (course == null) {
			return new ResponseUpdateCoursePages(false,
					"cannot find course with id " + courseId, "");
		}
		try {
			// 用来存储返回目录字符串的json array,其包含所有subcourse的信息
			JSONArray subcourseArray = JSON.parseArray(pages);
			// 用来存储最终存放进course表里pages字符串的json array,其只存id
			JSONArray resSubcourseArray = new JSONArray();
			// 遍历每一个一级子课程
			for (int i=0; i<subcourseArray.size(); i++) {
				JSONObject firstSubcourseObj = subcourseArray.getJSONObject(i);
				JSONObject firstResObj = new JSONObject();
				// 为每个一级子课程查询子课程id
				String title = firstSubcourseObj.getString("title");
				Subcourse firstSubcourse = getOrCreateSubcourseByTitleAndCourseId(title, courseId);
				firstSubcourseObj.put("id", firstSubcourse.getId());
				firstResObj.put("id", firstSubcourse.getId());
				JSONArray resSubcourses = new JSONArray();
				// 遍历每一个二级子课程
				JSONArray secondSubcourses = firstSubcourseObj.getJSONArray("subcourses");
				for (int j=0; j<secondSubcourses.size(); j++) {
					// 为每个二级子课程查询子课程id
					JSONObject secondSubcourseObj = secondSubcourses.getJSONObject(j);
					JSONObject secondResObj = new JSONObject();
					String secondtitle = secondSubcourseObj.getString("title");
					Subcourse secondSubcourse = getOrCreateSubcourseByTitleAndCourseId(secondtitle, courseId);
					secondSubcourseObj.put("id", secondSubcourse.getId());
					secondResObj.put("id", secondSubcourse.getId());
					resSubcourses.add(secondResObj);
				}
				firstResObj.put("subcourses", resSubcourses);
				resSubcourseArray.add(firstResObj);
			}

			String resPagesStr = JSON.toJSONString(resSubcourseArray);
			course.setPages(resPagesStr);
			courseDAO.update(course);

			String pagesStr = JSON.toJSONString(subcourseArray);
			return new ResponseUpdateCoursePages(true, "", pagesStr);
		} catch (Exception e) {
			return new ResponseUpdateCoursePages(false, e.getMessage(), "");
		}
	}

	private Subcourse getOrCreateSubcourseByTitleAndCourseId(String title, int courseId) {
		Subcourse subcourse = subcourseDAO.queryByTitleAndCourseId(courseId, title);
		if (subcourse != null) {
			return subcourse;
		} else {
			// 如果数据库里没有对应标题子课程，说明此目录项为新增的，需要为其创建子课程
			Subcourse newSubcourse = new Subcourse();
			newSubcourse.setTitle(title);
			newSubcourse.setCourseId(courseId);
			subcourseDAO.create(newSubcourse);
			return newSubcourse;
		}
	}

	private boolean isTeacherSessionKeyValid(String sessionKey) {
		Teacher teacher = teacherDAO.queryBySessionKey(sessionKey);
		return teacher == null;
	}
}
