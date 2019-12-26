package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.CourseMaterial;
import lombok.Data;

@Data
public class ResponseUploadMaterial extends BaseResponse {
	private CourseMaterial courseMaterial;

	public ResponseUploadMaterial(boolean success, String errMsg, CourseMaterial courseMaterial) {
		super(success, errMsg);
		this.courseMaterial = courseMaterial;
	}
}
