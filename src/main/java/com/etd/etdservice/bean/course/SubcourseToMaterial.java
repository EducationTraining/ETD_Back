package com.etd.etdservice.bean.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcourseToMaterial {
	private int id;
	private Integer materialId;
	private Integer subcourseId;
}
