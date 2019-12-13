package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

@Data
public class ResponseIsAttendCourse extends BaseResponse {
	private boolean isAttend;

	public ResponseIsAttendCourse(boolean success, String errMsg, boolean isAttend) {
		super(success, errMsg);
		this.isAttend = isAttend;
	}
}
