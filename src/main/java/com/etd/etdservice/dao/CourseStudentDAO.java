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
}
