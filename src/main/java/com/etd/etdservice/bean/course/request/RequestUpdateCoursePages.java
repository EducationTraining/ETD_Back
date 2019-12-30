package com.etd.etdservice.bean.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateCoursePages {
	// 老师sessionKey
	private String sessionKey;
	// 课程id
	private Integer courseId;
	// 课程目录json字符串
	private String pages;
}
