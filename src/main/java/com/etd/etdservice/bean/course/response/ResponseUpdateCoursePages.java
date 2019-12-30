package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseUpdateCoursePages extends BaseResponse {
	// 包含subcourse id和title的课程目录json string.
	private String pages;

	public ResponseUpdateCoursePages(boolean success, String errMsg, String pages) {
		super(success, errMsg);
		this.pages = pages;
	}
}
