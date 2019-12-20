package com.etd.etdservice.service;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudents;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.StudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.dao.UserDAOTest;
import com.etd.etdservice.serivce.CourseService;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    CourseDAO courseDAO;

    private  static TeacherDAO teacherDAO;
    @Autowired
    public void setTeacherDAO(TeacherDAO teacherDAO){
        this.teacherDAO = teacherDAO;
    }

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

    // 学生选课测试
    @Test
    public void attendCourseTest(){
        // mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();

        // mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);

        // 获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();

        BaseResponse baseResponse = courseService.attendCourse(courseId, sessionKey);
        assertEquals(baseResponse.isSuccess(),true);
        assertEquals("",baseResponse.getErrMsg());
    }

    // 学生退课测试
    @Test
    public void withdrawCourseTest(){
        // mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();

        // mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        // 获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        // 先插入选课表(选课)
        courseService.attendCourse(courseId, sessionKey);
        // 再删除选课表(退课)
        BaseResponse baseResponse = courseService.withdrawCourse(courseId,sessionKey);
        assertEquals(baseResponse.isSuccess(),true);
        assertEquals("",baseResponse.getErrMsg());
    }

    // 获取某学生是否参加了某门课程测试
    @Test
    public void isAttendCourseTest(){
        // mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();
        // mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        // 获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        // 先插入选课表
        courseService.attendCourse(courseId, sessionKey);
        // 再查询
        ResponseIsAttendCourse attendCourse = courseService.isAttendCourse(courseId,sessionKey);
        assertEquals(attendCourse.isSuccess(),true);
        assertEquals(attendCourse.isSuccess(),true);
        assertEquals(attendCourse.getErrMsg(),"");
    }

    // 获取某学生参加了的课程
    @Test
    public void getAttendedCoursesTest(){
        // mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();

        // mock两门课
        Course course1 = mockCourse();
        courseDAO.create(course1);
        int course1Id = courseDAO.queryByCourseNum(course1.getCourseNum()).getId();
        Course course2 = mockCourse();
        courseDAO.create(course2);
        int course2Id = courseDAO.queryByCourseNum(course2.getCourseNum()).getId();

        //该学生选取这两门课
        courseService.attendCourse(course1Id,sessionKey);
        courseService.attendCourse(course2Id,sessionKey);

        ResponseGetCourses responseGetCourses = courseService.getAttendedCourses(sessionKey);
        List<ResponseCourse> coursesList = responseGetCourses.getCoursesList();
        for (ResponseCourse responseCourse : coursesList) {
            log.info(responseCourse.toString());
        }
        assertTrue(responseGetCourses.isSuccess());
        assertEquals("",responseGetCourses.getErrMsg());
    }

    // 获取某门课参加的学生
    @Test
    public void getAttendStudentsTest(){
        // mock两个学生并插入students表中
        Student student1 = UserDAOTest.mockStudent();
        studentDAO.create(student1);
        Student student2 = UserDAOTest.mockStudent();
        studentDAO.create(student2);

        // mock一个老师并开设一门课程
        Course course = mockCourse();
        courseDAO.create(course);

        // 获取courseId和老师sessionKey
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        String sessionKey = teacherDAO.queryById(course.getTeacherId()).getSessionKey();

        // 两个学生选这门课
        courseService.attendCourse(courseId,student1.getSessionKey());
        courseService.attendCourse(courseId,student2.getSessionKey());

        ResponseGetStudents attendStudents = courseService.getAttendStudents(courseId, sessionKey);
        assertTrue(attendStudents.isSuccess());
        assertEquals("",attendStudents.getErrMsg());
        assertEquals(studentDAO.queryBySessionKey(student1.getSessionKey()).getId(),attendStudents.getStudentsList().get(0).getId());
        assertEquals(studentDAO.queryBySessionKey(student2.getSessionKey()).getId(),attendStudents.getStudentsList().get(1).getId());

    }

    //对某门课进行评价
    @Test
    public void remarkCourseTest(){
        // mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();
        // mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        // 获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        // 先插入选课表
        courseService.attendCourse(courseId, sessionKey);

        RequestRemarkCourse remark = new RequestRemarkCourse(courseId,sessionKey,"课程评价",99.4);
        BaseResponse baseResponse = courseService.remarkCourse(remark);
        assertEquals(baseResponse.isSuccess(),true);
        assertEquals("",baseResponse.getErrMsg());
    }
}
