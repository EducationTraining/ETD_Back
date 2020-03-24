package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseMaterial;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseMaterialDAOTest {
	@Autowired
	CourseMaterialDAO courseMaterialDAO;

	@Test
	public void testCreate() {
		CourseMaterial courseMaterial = mockCourseMaterialDAO();
		courseMaterialDAO.create(courseMaterial);
		assertTrue(courseMaterial.getId() > 0);
	}

	public static CourseMaterial mockCourseMaterialDAO() {
		CourseMaterial courseMaterial = new CourseMaterial();
		courseMaterial.setMaterialUrl(StringUtil.generateRandomString("url"));
		return courseMaterial;
	}
}
