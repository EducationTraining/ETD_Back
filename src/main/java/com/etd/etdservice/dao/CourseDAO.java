package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.Course;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseDAO {
	/**
	 * return count courses(use "limit #{count}" sql) order by field create_time.
	 * @param count
	 * @return a course List contains at most count courses
	 */
	List<Course> queryLatestCourses(int count);

	/**
	 * return count courses(use "limit #{count}" sql) order by field score.
	 * @param count
	 * @return a course List contains at most count courses
	 */
	List<Course> queryHottestCourses(int count);

	/**
	 * return all courses status = 1
	 * @return a Page<Course> Object
	 */
	Page<Course> queryValidCourses();

	/**
	 * return all courses
	 * @return a Page<Course> Object
	 */
	Page<Course> queryAllCourses();

	/**
	 * update course, located by id.
	 * @param course
	 * @return success or not
	 */
	boolean update(Course course);

	/**
	 * insert a course
	 * @param course
	 * @return success or not
	 */
	boolean create(Course course);
}
