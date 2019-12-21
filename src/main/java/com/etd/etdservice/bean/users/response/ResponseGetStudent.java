package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.dao.CourseDAO;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

	public static ResponseGetStudent fromBeanToResponse(Student bean) {
		ResponseGetStudent response = new ResponseGetStudent();
		if(bean == null) {
			response.setFailResponse(BaseResponse.NULL_STUDENT);
			return response;
		}
		response.setLatestTwoCourses(null);
		try {
			BeanUtils.copyProperties(bean, response);
			response.setSuccess(true);
			response.setErrMsg("");
		}catch (Exception e){
			response.setFailResponse(COPY_EXCEPTION);
			return response;
		}
		return response;
	}
}
