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

	/**
	 * 返回不包含最近两门课的学生信息
	 * @param bean
	 * @return
	 */
	public static ResponseGetStudent fromBeanToResponse(Student bean) {
		return fromBeanToResponse(bean, null);
	}

	/**
	 * 返回包含最近两门课的学生信息
	 * @param bean
	 * @param latestTwoCourses
	 * @return
	 */
	public static ResponseGetStudent fromBeanToResponse(Student bean, List<ResponseCourse> latestTwoCourses) {
		ResponseGetStudent response = new ResponseGetStudent();
		if (bean == null) {
			response.setFailResponse(BaseResponse.NULL_STUDENT);
			return response;
		}
		response.setLatestTwoCourses(latestTwoCourses);
		try {
			BeanUtils.copyProperties(bean, response);
			response.setSuccess(true);
			response.setErrMsg("");
		} catch (Exception e) {
			response.setFailResponse(COPY_EXCEPTION);
			return response;
		}
		return response;
	}
}
