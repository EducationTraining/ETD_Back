package com.etd.etdservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudent {
	private int id;
	private Integer courseId;
	private Integer studentId;
}
