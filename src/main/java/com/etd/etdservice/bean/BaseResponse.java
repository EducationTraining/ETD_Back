package com.etd.etdservice.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
	private boolean success;
	private String errMsg;

	public static final int NULL_SESSION_KEY = 1;
	public static final int NULL_TEACHER = 2;
	public static final int NULL_STUDENT = 3;
	public static final int NULL_ADMIN = 4;
	public static final int COPY_EXCEPTION = 5;

	public void setNullState() {
		this.success = false;
		this.errMsg = "null is not permitted";
	}

	public void setIllegalState() {
		this.success = false;
		this.errMsg = "illegal state is not permitted";
	}

	public BaseResponse setFailResponse(int FAIL_NUM) {
		this.setSuccess(false);
		String errorMessage;
		switch (FAIL_NUM) {
			case NULL_SESSION_KEY: errorMessage = "invalid sessionKey";
				break;
			case NULL_TEACHER: errorMessage = "null teacher";
				break;
			case NULL_STUDENT: errorMessage = "null student";
				break;
			case NULL_ADMIN: errorMessage = "null admin";
				break;
			case COPY_EXCEPTION: errorMessage = "can not copy object's field to response";
				break;
			default: errorMessage = "unknown error";
		}
		this.setErrMsg(errorMessage);
		return this;
	}
}
