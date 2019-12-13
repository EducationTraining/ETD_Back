package com.etd.etdservice.bean.users.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestUploadAvatar {
	private String sessionKey;
	private MultipartFile file;
}
