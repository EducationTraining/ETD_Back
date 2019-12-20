package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
		try{
			BeanUtils.copyProperties(bean, response);
		}catch (BeansException e){
			log.error("无法拷贝bean(Teacher)参数! " + "Teacher info:" + bean + " Error info: " + e.getMessage());
		}catch (IllegalArgumentException e){
			log.error("非法参数！无法拷贝teacher的变量. " + "Teacher info:" + bean + " Error info: " + e.getMessage());
			throw new IllegalArgumentException();
		}
		return response;
	}
}
