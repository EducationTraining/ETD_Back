package com.etd.etdservice.dao;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.serivce.CourseService;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface CourseDAO {
	Course queryById(int id);

	/**
	 * return count courses(use "limit #{count}" sql) order by field create_time.(status = 1)
	 * @param count
	 * @return a course List contains at most count courses
	 */
	List<Course> queryLatestCourses(int count);

	/**
	 * return count courses(use "limit #{count}" sql) order by field score.(status = 1)
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
	 * 根据课程编号查课程
	 * @return
	 */
	Course queryByCourseNum(String courseNum);

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

	/**
	 * queryTeacherByCourseId
	 * @param courseId
	 * @return Teacher
	 */
	Teacher queryTeacherByCourseId(Integer courseId);
}
