package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.users.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetStudent {
	int id;
	private String userName;
	private String realName;
	private String phone;
	private String email;
	private String avatarUrl;
	private String sex;
	// private List<Course> latestTwoCourses;

	public static ResponseGetStudent fromBeanToResponse(Student bean) {
		ResponseGetStudent response = new ResponseGetStudent();
		// response.setLastestTwoCourses(...);
		BeanUtils.copyProperties(response, bean);
		return response;
	}
}
