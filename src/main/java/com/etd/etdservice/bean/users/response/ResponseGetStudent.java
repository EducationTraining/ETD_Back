package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.users.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetStudent extends BaseResponse {
	int id;
	private String userName;
	private String realName;
	private String phone;
	private String email;
	private String avatarUrl;
	private String sex;
	private List<ResponseCourse> latestTwoCourses;

	public static ResponseGetStudent fromBeanToResponse(boolean success, String errMsg, List<ResponseCourse> latestTwoCourses,Student bean) {
		ResponseGetStudent response = new ResponseGetStudent();
		response.setLatestTwoCourses(latestTwoCourses);
		response.setSuccess(success);
		response.setErrMsg(errMsg);
		BeanUtils.copyProperties(bean, response);
		return response;
	}
}
