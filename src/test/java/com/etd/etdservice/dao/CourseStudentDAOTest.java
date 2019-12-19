package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseStudentDAOTest {

    @Autowired
    private CourseStudentDAO courseStudentDAO;


    public static CourseStudent mockCourseStudent() {
        CourseStudent courseStudent = new CourseStudent();
        int MAX_VALUE = 10000000;
        courseStudent.setCourseId(new Random().nextInt(MAX_VALUE));
        courseStudent.setStudentId(new Random().nextInt(MAX_VALUE));
        courseStudent.setCreateTime(new Date());
        return courseStudent;
    }

    @Test
    public void testCreate() {
        CourseStudent courseStudent=mockCourseStudent();
        courseStudentDAO.create(courseStudent);
        CourseStudent resCourseStudent=courseStudentDAO.getByCourseStudent(courseStudent);
        assertEquals(courseStudent.getCourseId(), resCourseStudent.getCourseId());
        assertEquals(courseStudent.getStudentId(),resCourseStudent.getStudentId());
    }


    @Test
    public void testGetCount() {
        CourseStudent courseStudent=mockCourseStudent();
        courseStudentDAO.create(courseStudent);
        int count=courseStudentDAO.getStudentCountsByCourseId(courseStudent.getCourseId());
        assertEquals(count,1);
    }
    @Test
    public void testDelete() {
        CourseStudent courseStudent=mockCourseStudent();
        courseStudentDAO.create(courseStudent);
        CourseStudent resCourseStudent=courseStudentDAO.getByCourseStudent(courseStudent);
        assertEquals(courseStudent.getCourseId(), resCourseStudent.getCourseId());
        assertEquals(courseStudent.getStudentId(), resCourseStudent.getStudentId());
        courseStudentDAO.delete(courseStudent);
        assertEquals(courseStudentDAO.getByCourseStudent(courseStudent),null);


    }

}
