package com.etd.etdservice.dao;

import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
public class CourseStudentRemarkDAOTest {
    @Autowired
    CourseStudentRemarkDAO courseStudentRemarkDAO;

    @Autowired
    CourseDAO courseDAO;

    @Test
    public void testQueryCourseAverageScore() {
        Course mockCourse = CourseDAOTest.mockCourse();
        courseDAO.create(mockCourse);

        CourseStudentRemark remark = mockCourseStudentRemark();
        remark.setCourseId(mockCourse.getId());
        remark.setStudentId(1);
        remark.setScore(4.0);
        courseStudentRemarkDAO.create(remark);
        remark = mockCourseStudentRemark();
        remark.setCourseId(mockCourse.getId());
        remark.setStudentId(2);
        remark.setScore(8.0);
        courseStudentRemarkDAO.create(remark);

        List<Course> courses = courseStudentRemarkDAO.queryCourseAverageScore();
        for (Course course: courses) {
            if (course.getId() == mockCourse.getId()) {
                assertTrue(course.getScore() == 6.0);
            }
        }
    }

    public static CourseStudentRemark mockCourseStudentRemark() {
        CourseStudentRemark courseStudentRemark = new CourseStudentRemark();
        courseStudentRemark.setRemark(StringUtil.generateRandomString("remark"));
        return courseStudentRemark;
    }

}
