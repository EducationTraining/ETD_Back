package com.etd.etdservice.bean.users.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateStudent {
	private String sessionKey;
	private String userName;
	private String realName;
	private String phone;
	private String email;
	private String sex;
}
