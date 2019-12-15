package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetTeacher {
	int id;
	private String userName;
	private String realName;
	private String phone;
	private String email;
	private String avatarUrl;
	private String description;

	public static ResponseGetTeacher fromBeanToResponse(Teacher bean) {
		ResponseGetTeacher response = new ResponseGetTeacher();
		BeanUtils.copyProperties(bean, response);
		return response;
	}
}
