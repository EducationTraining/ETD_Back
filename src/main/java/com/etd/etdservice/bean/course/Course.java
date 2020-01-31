package com.etd.etdservice.bean.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
	private int id;
	private String courseNum;
	private Integer teacherId;
	private String name;
	private Double score;
	private String pages;
	private Date startTime;
	private Integer weeks;
	private Integer status;
	private String description;
	private Date createTime;
	private String note;
	private String avatarUrl;
	private int categoryId;
}
