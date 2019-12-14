package com.etd.etdservice.bean.users.requests;

import com.etd.etdservice.bean.users.Student;
import lombok.*;
import org.springframework.beans.BeanUtils;

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
