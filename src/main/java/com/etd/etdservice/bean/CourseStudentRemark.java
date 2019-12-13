package com.etd.etdservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseStudentRemark {
	private int id;
	private Integer courseId;
	private Integer studentId;
	private String remark;
	private Double score;
}
