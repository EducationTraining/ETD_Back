package com.etd.etdservice.bean.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRemarkCourse {
	private Integer courseId;
	// 学生sessionKey
	private String sessionKey;
	private String remark;
	private Double score;
}
