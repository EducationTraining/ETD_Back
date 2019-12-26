package com.etd.etdservice.bean.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcourse {
	private int id;
	// 主课程id
	private Integer courseId;
	// 子课程标题
	private String title;
}
