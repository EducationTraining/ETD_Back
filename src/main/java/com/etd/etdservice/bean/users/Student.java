package com.etd.etdservice.bean.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	private int id;

	private String userName;

	private String sessionKey;

	private String realName;

	private String phone;

	private String email;

	private String avatarUrl;

	private String sex;

	private Boolean valid;

	private Date createTime;
}
