package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseStudentDAO {

	/**
	 * 传入一个CourseStudent类型的对象，返回表中匹配的记录
	 * @param courseStudent
	 * @return
	 */
	CourseStudent getByCourseStudent(CourseStudent courseStudent);

	/**
	 * insert a Course Student Object(选课记录).
	 * @param courseStudent
	 * @return success or not.
	 */
	boolean create(CourseStudent courseStudent);

	/**
	 * delete a Course Student Object(选课记录), located by courseId&studentId.
	 * @param courseStudent
	 * @return success or not.
	 */
	boolean delete(CourseStudent courseStudent);

	/**
	 * 根据课程id号返回选课学生的数量
	 * @param courseId
	 * @return
	 */
	Integer getStudentCountsByCourseId(int courseId);
}
