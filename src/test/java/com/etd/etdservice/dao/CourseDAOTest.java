package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseDAOTest {
	@Autowired
	private CourseDAO courseDAO;

	private static TeacherDAO teacherDAO;
	@Autowired
	public void setTeacherDAO(TeacherDAO teacherDAO){
		this.teacherDAO = teacherDAO;
	}


	private static final int COURSE_NUM = 5;
	private static final int COUNT = 5;



	public static Course mockCourse() {
		Course course = new Course();
		Teacher teacher  = UserDAOTest.mockTeacher();
		teacherDAO.create(teacher);
		Teacher teacherRes = teacherDAO.queryByUserName(teacher.getUserName());
		course.setTeacherId(teacherRes.getId());
		course.setCourseNum(StringUtil.generateRandomString("courseNUm"));
		course.setAvatarUrl(StringUtil.generateRandomString("avatarUrl"));
		course.setCreateTime(new Date());
		course.setStartTime(new Date());
		course.setDescription(StringUtil.generateRandomString("description"));
		course.setName(StringUtil.generateRandomString("name"));
		course.setNote(StringUtil.generateRandomString("note"));
		course.setPages(StringUtil.generateRandomString("pages"));
		course.setScore(DoubleUtil.nextDouble(0, 5));
		return course;
	}

	@Test
	public void testCreate() {
		Course course = mockCourse();
		assertTrue(courseDAO.create(course));
	}

	@Test
	public void testUpdate() {
		Course course = mockCourse();
		courseDAO.create(course);
		Course resCourse = courseDAO.queryByCourseNum(course.getCourseNum());
		String updatedName = "updated" + resCourse.getName();
		resCourse.setName(updatedName);
		assertTrue(courseDAO.update(resCourse));
		resCourse = courseDAO.queryById(resCourse.getId());
		assertEquals(resCourse.getName(), updatedName);
	}

	@Test
	public void testQueryCourses() {
		for (int i=0; i<COURSE_NUM; i++) {
			Course course = mockCourse();
			course.setStatus(1);
			courseDAO.create(course);
		}

		List<Course> courseList = courseDAO.queryHottestCourses(COUNT);
		assertEquals(courseList.size(), COUNT);
		log.info(courseList.toString());
		assertTrue(courseList.get(0).getScore() >= courseList.get(1).getScore());

		courseList = courseDAO.queryLatestCourses(COUNT);
		assertEquals(courseList.size(), COUNT);
		assertTrue(courseList.get(0).getCreateTime().getTime()
				>= courseList.get(1).getCreateTime().getTime());

		com.github.pagehelper.PageHelper.startPage(1, COUNT);
		Page<Course> courseListPage = courseDAO.queryAllCourses();
		courseList = courseListPage.getResult();
		assertEquals(courseList.size(), COUNT);

		Course courseRequest = courseList.get(0);
		courseRequest.setStatus(0);
		courseDAO.update(courseRequest);
		courseListPage = courseDAO.queryValidCourses();
		courseList = courseListPage.getResult();
		for (Course course: courseList) {
			assertEquals(1, (int) course.getStatus());
		}
	}


	@Test
	public void testDeleteByCourseId() {
		// 删除测试可能会导致相关联的信息不完整，虽然mockCourse方法会创建老师，
		// 但是它们之间的关系是：老师可以没有课程，但是课程必定属于一个老师
		// 因此可以直接删除课程
		Course mockCourse = mockCourse();
		courseDAO.create(mockCourse);
		mockCourse = courseDAO.queryByCourseNum(mockCourse.getCourseNum());
		Assert.assertNotNull(mockCourse);
		// 删除课程，判断不存在
		courseDAO.deleteByCourseId(mockCourse.getId());
		mockCourse = courseDAO.queryById(mockCourse.getId());
		Assert.assertNull(mockCourse);

	}

}
