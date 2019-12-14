package com.etd.etdservice.bean.users.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateTeacher {
	private String sessionKey;
	private String userName;
	private String realName;
	private String phone;
	private String email;
	private String description;
}
