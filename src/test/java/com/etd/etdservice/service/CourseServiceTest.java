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
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.CourseDAOTest;
import com.etd.etdservice.serivce.impl.CourseServiceImpl;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    private CourseDAO courseDAO;

    private  static TeacherDAO teacherDAO;
    @Autowired
    public  void setTeacherDAO(TeacherDAO teacherDAO) {
        CourseServiceTest.teacherDAO = teacherDAO;
    }


    @Value("${image_root_path}")
    private String imageRootPath;

    @Value("${url_starter}")
    private String urlStarter;

    private static final int COURSE_NUM = 5;

    private static final int LIMIT = 3;

    private static final int MAX_PAGE = 10;


    public static Course mockCourse(){
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
    public void remarkCourseTest() {
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

        RequestRemarkCourse remark = new RequestRemarkCourse(courseId, sessionKey, "课程评价", 99.4);
        BaseResponse baseResponse = courseService.remarkCourse(remark);
        assertEquals(baseResponse.isSuccess(), true);
        assertEquals("", baseResponse.getErrMsg());
    }

    @Test
    public void testGetHottestCourses() {
        for(int i = 0; i < COURSE_NUM; ++i) {
            Course mockCourse = CourseServiceTest.mockCourse();
            courseDAO.create(mockCourse);
        }

        List<Course> courses = courseDAO.queryAllCourses();
        Course mockCourse = courses.get(0);
        mockCourse.setStatus(1);
        mockCourse.setScore(5.0);
        log.info("modified course: " + mockCourse);

        courseDAO.update(mockCourse);

        ResponseGetCourses responseGetCourses = courseService.getHottestCourses();
        Assert.assertTrue(responseGetCourses.isSuccess());
        Assert.assertNotNull(responseGetCourses.getCoursesList());

        boolean flag = false;
        List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
        for(ResponseCourse course : courseList) {
            if(course.getId() == mockCourse.getId()){
                flag = true;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testGetLatestCourses(){
        Course mockCourse = CourseServiceTest.mockCourse();
        courseDAO.create(mockCourse);
        ResponseGetCourses responseGetCourses = courseService.getLatestCourses();
        Assert.assertTrue(responseGetCourses.isSuccess());
        Assert.assertNotNull(responseGetCourses.getCoursesList());

        boolean flag = false;
        List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
        for(int i = 0; i < courseList.size(); ++i){
            if(courseList.get(i).getId() == mockCourse.getId()){
                ResponseCourse responseCourse = courseList.get(i);
                Assert.assertEquals(responseCourse.getTeacher().getId(), (int)mockCourse.getTeacherId());
            }
        }
    }

    @Test
    public void testGetCourses(){
        for(int i = 0; i < LIMIT * MAX_PAGE; ++i){
            Course course = mockCourse();
            courseDAO.create(course);
        }
        for(int page = 1; page < MAX_PAGE; ++page){
            ResponseGetCourses responseGetCourses = courseService.getCourses(page, LIMIT);

            Assert.assertTrue(responseGetCourses.isSuccess());
            List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
            Assert.assertEquals(LIMIT, courseList.size());
        }
    }

    @Test
    public void testUploadCoursePic(){
        Course course = mockCourse();
        courseDAO.create(course);
        course = courseDAO.queryByCourseNum(course.getCourseNum());
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());

        String name = StringUtil.generateRandomString("name:");
        String originalName = StringUtil.generateRandomString("originalName");
        String contentType = "testContent";
        byte[] content = StringUtil.generateRandomString("content:").getBytes();
        MultipartFile file = new MockMultipartFile(name, originalName, contentType, content);

        ResponseUploadAvatar responseUploadAvatar =
                courseService.uploadCoursePic(file, course.getId(), teacher.getSessionKey());

        Assert.assertNotNull(responseUploadAvatar);
        Assert.assertTrue(responseUploadAvatar.isSuccess());
        String expectedAvatarUrl =
                urlStarter + "/images/" + CourseServiceImpl.COURSE_TYPE + "/" + file.getOriginalFilename();
        Assert.assertEquals(responseUploadAvatar.getAvatarUrl(), expectedAvatarUrl);

        String filePath = imageRootPath + "/"+CourseServiceImpl.COURSE_TYPE+"/" + file.getOriginalFilename();
        File uploadedFile = new File(filePath);
        log.info("Test uploaded file's path: " + uploadedFile.getAbsolutePath());
        Assert.assertTrue(uploadedFile.exists());
    }

    @Test
    public void testUpdateCourseInfo(){
        Course mockCourse = mockCourse();
        //course与mockCourse都是随机生成的。
        // 将course插入数据库，然后用mockCourse的信息作为course的更新信息
        //更新后比对course更新后的信息与mockCourse是否一致
        Course course = mockCourse();
        mockCourse.setStatus(1);
        courseDAO.create(course);

        course = courseDAO.queryByCourseNum(course.getCourseNum());
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());

        RequestUpdateCourse request = new RequestUpdateCourse();

        //无传入信息，应当返回false
        BaseResponse response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());

        //只有老师的sessionKey，无courseId，返回false
        request.setSessionKey(teacher.getSessionKey());
        response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());

        //老师的sessionKey和课程的courseId都有
        request.setCourseId(course.getId());
        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        //只更新一项
        request.setName(mockCourse.getName());
        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        request.setPages(mockCourse.getPages());
        request.setStartTime(mockCourse.getStartTime().getTime());
        request.setWeeks(mockCourse.getWeeks());
        request.setStatus(mockCourse.getStatus());
        request.setDescription(mockCourse.getDescription());
        request.setNote(mockCourse.getNote());
        request.setAvatarUrl(mockCourse.getAvatarUrl());
        request.setCourseId(course.getId());

        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        course = courseDAO.queryById(course.getId());
        Assert.assertEquals(mockCourse.getName(), course.getName());
        Assert.assertEquals(mockCourse.getPages(), course.getPages());
        //startTime比较存在一定的出入
        Assert.assertEquals(mockCourse.getStartTime().toString(), course.getStartTime().toString());
        Assert.assertEquals(mockCourse.getWeeks(), course.getWeeks());
        Assert.assertEquals(mockCourse.getStatus(), course.getStatus());
        Assert.assertEquals(mockCourse.getDescription(), course.getDescription());
        Assert.assertEquals(mockCourse.getNote(), course.getNote());
        Assert.assertEquals(mockCourse.getAvatarUrl(), course.getAvatarUrl());

        //测试course部分更新
        request.setStartTime(null);
        request.setWeeks(null);
        request.setDescription(null);
        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        //设置错误courseId，应当无法更新，返回false
        request.setCourseId(49521354);
        response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());
    }
}
