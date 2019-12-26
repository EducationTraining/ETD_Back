package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.Subcourse;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SubcourseDAOTest {
	@Autowired
	SubcourseDAO subcourseDAO;

	@Test
	public void testCreate() {
		Subcourse subcourse = mockSubcourse();
		subcourseDAO.create(subcourse);
		assertTrue(subcourse.getId() > 0);
	}

	@Test
	public void testUpdate() {
		Subcourse subcourse = mockSubcourse();
		subcourseDAO.create(subcourse);
		String updatedTitle = subcourse.getTitle() + "updated";
		subcourse.setTitle(updatedTitle);
		subcourseDAO.update(subcourse);
		subcourse = subcourseDAO.queryById(subcourse.getId());
		assertEquals(subcourse.getTitle(), updatedTitle);
	}

	public static Subcourse mockSubcourse() {
		Subcourse subcourse = new Subcourse();
		subcourse.setCourseId(1);
		subcourse.setTitle(StringUtil.generateRandomString("title"));

		return subcourse;
	}
}
