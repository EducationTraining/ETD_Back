package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseGetCourses extends BaseResponse {
	private List<ResponseCourse> coursesList;

	public ResponseGetCourses(boolean success, String errMsg, List<ResponseCourse> coursesList) {
		super(success, errMsg);
		this.coursesList = coursesList;
	}
}
