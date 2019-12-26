package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseMaterial;
import com.etd.etdservice.bean.course.Subcourse;
import com.etd.etdservice.bean.course.SubcourseToMaterial;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SubcourseToMaterialDAOTest {
	@Autowired
	private SubcourseDAO subcourseDAO;
	@Autowired
	private CourseMaterialDAO materialDAO;
	@Autowired
	private SubcourseToMaterialDAO subcourseToMaterialDAO;

	private Subcourse subcourse;
	private CourseMaterial courseMaterial;

	@Before
	public void prepare() {
		subcourse = SubcourseDAOTest.mockSubcourse();
		courseMaterial = CourseMaterialDAOTest.mockCourseMaterialDAO();
	}

	@Test
	public void testCreate() {
		SubcourseToMaterial subcourseToMaterial = new SubcourseToMaterial();
		subcourseDAO.create(subcourse);
		materialDAO.create(courseMaterial);

		subcourseToMaterial.setMaterialId(courseMaterial.getId());
		subcourseToMaterial.setSubcourseId(subcourse.getId());
		subcourseToMaterialDAO.create(subcourseToMaterial);
		assertTrue(subcourseToMaterial.getId() > 0);
	}

	@Test
	public void testQueryBySubcourseId() {
		SubcourseToMaterial subcourseToMaterial = new SubcourseToMaterial();
		subcourseDAO.create(subcourse);
		materialDAO.create(courseMaterial);
		subcourseToMaterial.setMaterialId(courseMaterial.getId());
		subcourseToMaterial.setSubcourseId(subcourse.getId());
		subcourseToMaterialDAO.create(subcourseToMaterial);
		courseMaterial.setId(-1);
		materialDAO.create(courseMaterial);
		subcourseToMaterial.setMaterialId(courseMaterial.getId());
		subcourseToMaterialDAO.create(subcourseToMaterial);

		List<CourseMaterial> resList = subcourseToMaterialDAO.queryMaterialsBySubcourseId(subcourseToMaterial.getSubcourseId());
		assertTrue(resList.size() >= 2);
	}
}
