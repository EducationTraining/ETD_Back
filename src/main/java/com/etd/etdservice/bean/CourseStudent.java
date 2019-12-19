package com.etd.etdservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudent {
	private int id;
	private Integer courseId;
	private Integer studentId;
	private Date createTime;
}
