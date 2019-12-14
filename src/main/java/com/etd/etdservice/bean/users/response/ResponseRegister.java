package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

@Data
public class ResponseRegister extends BaseResponse {
	private String sessionKey;

	public ResponseRegister(boolean success, String errMsg, String sessionKey) {
		super(success, errMsg);
		this.sessionKey = sessionKey;
	}
}
