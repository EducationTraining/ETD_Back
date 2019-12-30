package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCourse {
	int id;
	private String courseNum;
	private ResponseGetTeacher teacher;
	private Integer studentNum;
	private String name;
	private Double score;
	private String pages;
	private Long startTime;
	private Integer weeks;
	private String description;
	private String note;
	private String avatarUrl;

	/**
	 *
	 * @param bean
	 * @param teacher
	 * @param studentNum 上课人数
	 * @param processedPages 带标题和subcourse id的课程目录json字符串
	 * @return
	 */
	public static ResponseCourse fromBeanToResponse(Course bean, ResponseGetTeacher teacher, Integer studentNum, String processedPages) {
		ResponseCourse response = new ResponseCourse();
		BeanUtils.copyProperties(bean, response);
		if (bean.getStartTime() != null) {
			response.setStartTime(bean.getStartTime().getTime());
		}
		response.setTeacher(teacher);
		response.setStudentNum(studentNum);
		response.setPages(processedPages);
		return response;
	}
}
