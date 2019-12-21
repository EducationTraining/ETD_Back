package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudentRemark;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CourseStudentRemarkDAO {
	/**
	 * insert a CourseStudentRemark Object(课程评价).
	 * @param courseStudentRemark
	 * @return success or not
	 */
	boolean create(CourseStudentRemark courseStudentRemark);

	/**
	 * update a CourseStudentRemark Object(课程评价), located by courseId&studentId.
	 * @param courseStudentRemark
	 * @return success or not
	 */
	boolean update(CourseStudentRemark courseStudentRemark);
}
