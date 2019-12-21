package com.etd.etdservice.bean.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	private int id;

	private String userName;

	private String sessionKey;

	private String phone;

	private String email;

	private Date createTime;
}
