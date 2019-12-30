package com.etd.etdservice.web;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCoursePages;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseUpdateCoursePages;
import com.etd.etdservice.bean.course.response.ResponseUploadMaterial;
import com.etd.etdservice.bean.users.response.ResponseGetStudents;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.serivce.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {
	@Autowired
	CourseService service;

	@RequestMapping(value = "/hottest-courses", method = RequestMethod.GET)
	public ResponseGetCourses getHottestCourses() {
		return service.getHottestCourses();
	}

	@RequestMapping(value = "/latest-courses", method = RequestMethod.GET)
	public ResponseGetCourses getLatestCourses() {
		return service.getLatestCourses();
	}

	@RequestMapping(value = "/courses", method = RequestMethod.GET)
	public ResponseGetCourses getLatestCourses(@RequestParam(value = "page", defaultValue = "1") Integer page,
	                                           @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
		return service.getCourses(page, limit);
	}

	@RequestMapping(value = "/upload-avatar", method = RequestMethod.POST)
	public ResponseUploadAvatar uploadAvatarForStudent(@RequestParam("file") MultipartFile file,
	                                                   @RequestParam("courseId")Integer courseId,
	                                                   @RequestParam("sessionKey") String sessionKey) {
		return service.uploadCoursePic(file, courseId, sessionKey);
	}

	@RequestMapping(value = "/upload-material", method = RequestMethod.POST)
	public ResponseUploadMaterial uploadCourseMaterial(@RequestParam("file") MultipartFile video,
	                                                   @RequestParam("subcourseId")Integer subcourseId,
	                                                   @RequestParam("sessionKey") String sessionKey) {
		return service.uploadSubcourseMaterial(video, subcourseId, sessionKey);
	}

	@RequestMapping(value = "/course", method = RequestMethod.POST)
	public BaseResponse updateCourseInfo(@RequestBody RequestUpdateCourse request) {
		return service.updateCourseInfo(request);
	}

	@RequestMapping(value = "/attend", method = RequestMethod.POST)
	public BaseResponse attendCourse(@RequestParam("courseId") int courseId, @RequestParam("sessionKey")String sessionKey) {
		return service.attendCourse(courseId, sessionKey);
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public BaseResponse withdrawCourse(@RequestParam("courseId") int courseId, @RequestParam("sessionKey")String sessionKey) {
		return service.withdrawCourse(courseId, sessionKey);
	}

	@RequestMapping(value = "/isattend", method = RequestMethod.GET)
	public BaseResponse isAttendCourse(@RequestParam("courseId") int courseId, @RequestParam("sessionKey")String sessionKey) {
		return service.isAttendCourse(courseId, sessionKey);
	}

	@RequestMapping(value = "/session-key/{sessionKey}/attend-courses", method = RequestMethod.GET)
	public BaseResponse getAttendedCourses(@PathVariable String sessionKey) {
		return service.getAttendedCourses(sessionKey);
	}

	@RequestMapping(value = "/attend-students", method = RequestMethod.GET)
	public ResponseGetStudents getAttendStudents(@RequestParam("courseId") int courseId, @RequestParam("sessionKey")String sessionKey) {
		return service.getAttendStudents(courseId, sessionKey);
	}

	@RequestMapping(value = "/remark", method = RequestMethod.POST)
	public BaseResponse remarkCourse(@RequestBody RequestRemarkCourse request) {
		return service.remarkCourse(request);
	}

	@RequestMapping(value = "/pages", method = RequestMethod.POST)
	public ResponseUpdateCoursePages updateCoursePages(@RequestBody RequestUpdateCoursePages request) {
		String sessionKey = request.getSessionKey();
		String pages = request.getPages();
		Integer courseId = request.getCourseId();
		return service.updateCoursePages(pages, courseId, sessionKey);
	}

}
