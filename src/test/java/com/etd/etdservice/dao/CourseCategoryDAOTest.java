package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseCategory;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseCategoryDAOTest {
    @Autowired
    private CourseCategoryDAO courseCategoryDAO;

    public static CourseCategory mockCourseCategory() {
        CourseCategory courseCategory = new CourseCategory();
        int MAX_VALUE = 1000000;
        courseCategory.setCategoryId(new Random().nextInt(MAX_VALUE));
        courseCategory.setCategoryName(StringUtil.generateRandomString("CategoryName"));
        return courseCategory;
    }

    @Test
    public void queryAllCategoryNameTest() {
        CourseCategory courseCategory = mockCourseCategory();
        courseCategoryDAO.addCourseCategory(courseCategory);
        List<String> names = courseCategoryDAO.queryAllCategoryName();
        for (String name : names) {
            log.info(name);
        }
    }

    @Test
    public void addCourseCategoryTest() {
        CourseCategory courseCategory = mockCourseCategory();
        assertTrue(courseCategoryDAO.addCourseCategory(courseCategory));
    }

    @Test
    public void updateCourseCategoryTest() {
        CourseCategory courseCategory = mockCourseCategory();
        int categoryId = courseCategory.getCategoryId();
        courseCategoryDAO.addCourseCategory(courseCategory);
        courseCategoryDAO.updateCategoryName(categoryId,"updateName");
    }

    @Test
    public void deleteCourseCategoryTest() {
        CourseCategory courseCategory = mockCourseCategory();
        int categoryId = courseCategory.getCategoryId();
        courseCategoryDAO.addCourseCategory(courseCategory);
        assertTrue(courseCategoryDAO.deleteCourseCategory(categoryId));
    }

}
