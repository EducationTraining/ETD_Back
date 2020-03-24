package com.etd.etdservice.bean.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseMaterial {
	private int id;
	private String materialUrl;
	private String type;
}
