package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUploadCourse extends BaseResponse {
	private Integer courseId;

	public ResponseUploadCourse(boolean success, String errMsg, Integer courseId) {
		super(success, errMsg);
		this.courseId = courseId;
	}
}
