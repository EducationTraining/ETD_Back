package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.Student;
import lombok.experimental.var;
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

	/**
	 * 选课
	 * @param courseStudent
	 * @return
	 */
	boolean attendCourse(CourseStudent courseStudent);

	/**
	 * 退课
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	boolean withdrawCourse(Integer courseId, Integer studentId);

	/**
	 * 获取某学生是否参加了某门课程
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	CourseStudent isAttendCourse(Integer courseId, Integer studentId);

	/**
	 * 获取某学生参加了的课程
	 * @param studentId
	 * @return
	 */
	List<Course> getAttendedCourses(Integer studentId);

	/**
	 * 获取某门课参加的学生
	 */
	List<Student> getAttendStudents(Integer courseId);

	/**
	 * 对某门课进行评价
	 * @param courseStudentRemark
	 * @return
	 */
    boolean remarkCourse(CourseStudentRemark courseStudentRemark);

	List<Integer> queryCourseIdByStudentId(Integer studentId);

}
