package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

@Data
public class ResponseUploadAvatar extends BaseResponse {
	private String avatarUrl;

	public ResponseUploadAvatar(boolean success, String errMsg, String avatarUrl) {
		super(success, errMsg);
		this.avatarUrl = avatarUrl;
	}
}
