package com.etd.etdservice.bean.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateCourse {
	private int courseId;
	// 老师sessionKey
	private String sessionKey;
	private String name;
	private String pages;
	private Long startTime;
	private Integer weeks;
	private Integer status;
	private String description;
	private String note;
	private String avatarUrl;
}
