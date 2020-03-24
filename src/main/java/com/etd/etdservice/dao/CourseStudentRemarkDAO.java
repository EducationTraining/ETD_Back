package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.course.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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

	/**
	 * 根据remark表查询表里课程的平均分
	 * @return
	 */
	List<Course> queryCourseAverageScore();
}
