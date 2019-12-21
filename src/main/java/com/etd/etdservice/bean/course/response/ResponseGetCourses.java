package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ResponseGetCourses extends BaseResponse {
	private List<ResponseCourse> coursesList;

	public ResponseGetCourses(boolean success, String errMsg, List<ResponseCourse> coursesList) {
		super(success, errMsg);
		this.coursesList = coursesList;
	}
}
