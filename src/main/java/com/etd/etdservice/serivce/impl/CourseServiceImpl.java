package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.response.ResponseGetStudent;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.serivce.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
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
	public BaseResponse updateCourseInfo(RequestUpdateCourse request, String sessionKey) {
		return null;
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
