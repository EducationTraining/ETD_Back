package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseStudentDAOTest {

    @Autowired
    private CourseStudentDAO courseStudentDAO;

    private static CourseStudent mockCourseStudent(){
        Random random = new Random();
        int MAX_VALUE = 10000000;
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(random.nextInt(MAX_VALUE));
        courseStudent.setStudentId(random.nextInt(MAX_VALUE));
        return courseStudent;
    }

    @Test
    public void testCourseStudent(){
        CourseStudent courseStudent = mockCourseStudent();
        courseStudentDAO.create(courseStudent);

        CourseStudent expectedCourseStudent = courseStudentDAO.queryExistOrNot(courseStudent);
        Assert.assertNotNull(expectedCourseStudent);
        Assert.assertEquals(courseStudent.getCourseId(), expectedCourseStudent.getCourseId());
        Assert.assertEquals(courseStudent.getStudentId(), expectedCourseStudent.getStudentId());

        courseStudentDAO.delete(courseStudent);
        expectedCourseStudent = courseStudentDAO.queryExistOrNot(courseStudent);
        Assert.assertNull(expectedCourseStudent);

        courseStudentDAO.create(courseStudent);
        Integer studentNumbers = courseStudentDAO.queryStudentNumbersByCourseId(courseStudent.getCourseId());
        Assert.assertEquals(Integer.valueOf(1), studentNumbers);
    }
}
