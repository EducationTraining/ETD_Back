package com.etd.etdservice.bean.course.request;

import com.etd.etdservice.bean.course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUploadCourse {
	// 老师sessionKey
	private String sessionKey;
	private String name;
	private String pages;
	private Long startTime;
	private Integer weeks;
	private String description;
	private String note;

	public static Course fromRequestToBean(RequestUploadCourse request, int teacherId) {
		Course bean = new Course();
		BeanUtils.copyProperties(request, bean);
		if (request.getStartTime() != null) {
			bean.setStartTime(new Date(request.getStartTime()));
		}
		bean.setCreateTime(new Date());
		bean.setTeacherId(teacherId);
		return bean;
	}
}
