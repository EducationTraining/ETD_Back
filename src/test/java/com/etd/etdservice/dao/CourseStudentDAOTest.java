package com.etd.etdservice.dao;



import com.etd.etdservice.bean.CourseStudent;
import com.etd.etdservice.bean.CourseStudentRemark;
import com.etd.etdservice.bean.users.Student;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
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


    public static CourseStudentRemark mockCourseStudentRemark() {
        CourseStudentRemark courseStudentRemark = new CourseStudentRemark();
        int MAX_VALUE = 10000000;
        courseStudentRemark.setCourseId(new Random().nextInt(MAX_VALUE));
        courseStudentRemark.setStudentId(new Random().nextInt(MAX_VALUE));
        courseStudentRemark.setRemark("testRemark");
        courseStudentRemark.setScore(99.5);
        return courseStudentRemark;
    }

    // 学生选课测试
    @Test
    public void attendCourseTest(){
        CourseStudent courseStudent = mockCourseStudent();
        boolean status = courseStudentDAO.attendCourse(courseStudent);
        assertEquals(true,status);
    }

    // 学生退课测试
    @Test
    public void withdrawCourseTest(){
        CourseStudent courseStudent = mockCourseStudent();
        int studentId = courseStudent.getStudentId();
        int courseId = courseStudent.getCourseId();
        courseStudentDAO.attendCourse(courseStudent);
        boolean status = courseStudentDAO.withdrawCourse(courseId, studentId);
        assertEquals(true,status);
    }

    // 获取某学生是否参加了某门课程
    @Test
    public void isAttendCourseTest(){
        CourseStudent courseStudent = mockCourseStudent();
        int studentId = courseStudent.getStudentId();
        int courseId = courseStudent.getCourseId();
        courseStudentDAO.attendCourse(courseStudent);
        CourseStudent attendCourse = courseStudentDAO.isAttendCourse(courseId, studentId);
        assertEquals(courseStudent.getCourseId(),attendCourse.getCourseId());
        assertEquals(courseStudent.getStudentId(),attendCourse.getStudentId());
    }

    // 获取某学生参加了的课程
    @Test
    public void getAttendedCoursesTest(){
        // 我没有getAttendedCourses这个Dao，不知道该怎么测试Dao
    }

    // 获取某门课参加的学生
    @Test
    public void getAttendStudentsTest(){
        CourseStudent courseStudent = mockCourseStudent();
        int studentId = courseStudent.getStudentId();
        int courseId = courseStudent.getCourseId();
        courseStudentDAO.attendCourse(courseStudent);
        List<Student> attendStudents = courseStudentDAO.getAttendStudents(courseId);
        for (Student attendStudent : attendStudents) {
            assertEquals(studentId,attendStudent.getId());
        }
    }

    //对某门课进行评价
    @Test
    public void remarkCourseTest(){
        CourseStudentRemark courseStudentRemark = mockCourseStudentRemark();
        boolean status = courseStudentDAO.remarkCourse(courseStudentRemark);
        assertEquals(true,status);
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
