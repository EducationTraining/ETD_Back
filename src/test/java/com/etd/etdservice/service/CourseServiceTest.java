package com.etd.etdservice.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.CourseNote;
import com.etd.etdservice.bean.course.request.RequestPublishCourseNote;
import com.etd.etdservice.bean.course.request.RequestRemarkCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourseNote;
import com.etd.etdservice.bean.course.response.*;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetStudents;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.*;
import com.etd.etdservice.serivce.CourseService;
import com.etd.etdservice.serivce.impl.CourseServiceImpl;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;


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

    @Autowired
    private SubcourseDAO subcourseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private CourseNoteDAO courseNoteDAO;

    @Value("${image_root_path}")
    private String imageRootPath;

    @Value("${url_starter}")
    private String urlStarter;

    private static final int COURSE_NUM = 5;

    private static final int LIMIT = 3;

    private static final int MAX_PAGE = 10;


    /**
     * mockCourse 返回的 course 不包含有效的 teacher id。
     * @return
     */
    public static Course mockCourse() {
        Course course = new Course();
        course.setCourseNum(StringUtil.generateRandomString("courseNUm"));
        course.setTeacherId(1);
        course.setName(StringUtil.generateRandomString("name"));
        course.setScore(DoubleUtil.nextDouble(0, 5));
        course.setPages(StringUtil.generateRandomString("pages"));
        course.setStartTime(new Date());
        course.setWeeks(new Random().nextInt(10));
        course.setStatus(new Random().nextInt(2));
        course.setDescription(StringUtil.generateRandomString("description"));
        course.setCreateTime(new Date());
        course.setNote(StringUtil.generateRandomString("note"));
        course.setAvatarUrl(StringUtil.generateRandomString("avatarUrl"));
        return course;
    }

    /**
     * 根据此方法生成的 course，其所属的 teacher 是真实存在的
     * @return 课程 course
     */
    private Course mockCourseWithTrueTeacher() {
        return mockCourseWithTrueTeacher(null);
    }

    /**
     * 根据此方法生成的 course，其所属的 teacher 由参数传入
     * @param teacher 课程所属老师
     * @return
     */
    private Course mockCourseWithTrueTeacher(Teacher teacher) {
        Course course = mockCourse();
        if (teacher == null || teacher.getSessionKey() == null) {
            teacher = UserDAOTest.mockTeacher();
        }
        if (teacherDAO.queryBySessionKey(teacher.getSessionKey()) == null) {
            teacherDAO.create(teacher);
        }
        teacher = teacherDAO.queryBySessionKey(teacher.getSessionKey());
        course.setTeacherId(teacher.getId());
        courseDAO.create(course);
        return course;
    }

    // 学生选课测试
    @Test
    public void attendCourseTest() {
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
        assertTrue(baseResponse.isSuccess());
        assertEquals("", baseResponse.getErrMsg());
    }

    // 学生退课测试
    @Test
    public void withdrawCourseTest() {
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
        assertTrue(baseResponse.isSuccess());
        assertEquals("", baseResponse.getErrMsg());
    }

    // 获取某学生是否参加了某门课程测试
    @Test
    public void isAttendCourseTest() {
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
        assertTrue(attendCourse.isSuccess());
        assertEquals(attendCourse.getErrMsg(),"");
    }

    // 获取某学生参加了的课程
    @Test
    public void getAttendedCoursesTest() {
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
        courseService.attendCourse(course1Id, sessionKey);
        courseService.attendCourse(course2Id, sessionKey);

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
    public void getAttendStudentsTest() {
        // mock两个学生并插入students表中
        Student student1 = UserDAOTest.mockStudent();
        studentDAO.create(student1);
        Student student2 = UserDAOTest.mockStudent();
        studentDAO.create(student2);
        Teacher teacher = UserDAOTest.mockTeacher();
        teacherDAO.create(teacher);

        // mock一个老师并开设一门课程
        Course course = mockCourseWithTrueTeacher();
        log.debug("courseId: " + course.getId() + "\nteacherId: " + teacher.getId());

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

        log.info(attendStudents.getStudentsList().toString());
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
        assertTrue(baseResponse.isSuccess());
        assertEquals("", baseResponse.getErrMsg());
    }

    @Test
    public void testGetHottestCourses() {
        for (int i = 0; i < COURSE_NUM; ++i) {
            mockCourseWithTrueTeacher();
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
        for (ResponseCourse course : courseList) {
            if (course.getId() == mockCourse.getId()){
                flag = true;
            }
        }
        Assert.assertTrue(flag);
    }

    @Test
    public void testGetLatestCourses() {
        Course mockCourse = null;
        for (int i = 0; i < COURSE_NUM; ++i) {
            mockCourse = mockCourseWithTrueTeacher();
        }

        ResponseGetCourses responseGetCourses = courseService.getLatestCourses();
        log.debug("Debug getLatestCourses: " + JSON.toJSONString(responseGetCourses));

        Assert.assertTrue(responseGetCourses.isSuccess());
        Assert.assertNotNull(responseGetCourses.getCoursesList());

        List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
        for (int i = 0; i < courseList.size(); ++i){
            if (courseList.get(i).getId() == mockCourse.getId()){
                ResponseCourse responseCourse = courseList.get(i);
                Assert.assertEquals(responseCourse.getTeacher().getId(), (int)mockCourse.getTeacherId());
            }
        }

    }

    @Test
    public void testGetCourses() {
        for (int i = 0; i < LIMIT * MAX_PAGE; ++i){
            mockCourse();
        }
        for (int page = 1; page < MAX_PAGE; ++page){
            ResponseGetCourses responseGetCourses = courseService.getCourses(page, LIMIT);

            Assert.assertTrue(responseGetCourses.isSuccess());
            List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
            Assert.assertEquals(LIMIT, courseList.size());
        }
    }

    @Test
    public void testUploadCoursePic() {
        Course course = mockCourseWithTrueTeacher();
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
    public void testUpdateCourseInfo() {
        Course mockCourse = mockCourse();
        // course与mockCourse都是随机生成的。
        // 将course插入数据库，然后用mockCourse的信息作为course的更新信息
        // 更新后比对course更新后的信息与mockCourse是否一致
        Course course = mockCourseWithTrueTeacher();
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());

        course = courseDAO.queryByCourseNum(course.getCourseNum());

        RequestUpdateCourse request = new RequestUpdateCourse();

        // 无传入信息，应当返回false
        BaseResponse response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());

        // 只有老师的sessionKey，无courseId，返回false
        request.setSessionKey(teacher.getSessionKey());
        response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());

        // 老师的sessionKey和课程的courseId都有
        request.setCourseId(course.getId());
        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        // 只更新一项
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
        // startTime比较存在一定的出入
        log.debug(mockCourse.getStartTime().toString() + course.getStartTime().toString());
        Assert.assertEquals(mockCourse.getStartTime().toString().substring(0, 14),
                course.getStartTime().toString().substring(0, 14));
        Assert.assertEquals(mockCourse.getWeeks(), course.getWeeks());
        Assert.assertEquals(mockCourse.getStatus(), course.getStatus());
        Assert.assertEquals(mockCourse.getDescription(), course.getDescription());
        Assert.assertEquals(mockCourse.getNote(), course.getNote());
        Assert.assertEquals(mockCourse.getAvatarUrl(), course.getAvatarUrl());

        // 测试course部分更新
        request.setStartTime(null);
        request.setWeeks(null);
        request.setDescription(null);
        response = courseService.updateCourseInfo(request);
        Assert.assertTrue(response.isSuccess());

        // 设置错误courseId，应当无法更新，返回false
        request.setCourseId(49521354);
        response = courseService.updateCourseInfo(request);
        Assert.assertFalse(response.isSuccess());
    }

    @Test
    public void testUpdateCoursePages() {
        // mock
        JSONArray mockSubcourseArray = new JSONArray();
        Course course = mockCourseWithTrueTeacher();
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());
        // Comment courseDAO.create(course) and use next line to test update pages for same course.
        // course.setId(142);
        for (int i=1; i<=3; i++) {
            JSONObject firstSubcourseObj = new JSONObject();
            firstSubcourseObj.put("title", "title" + i);
            JSONArray secondSubcourseArray = new JSONArray();
            for (int j=1; j<=3; j++) {
                JSONObject secondSubcourseObj = new JSONObject();
                secondSubcourseObj.put("title", "title" + i + "." + j);
                secondSubcourseArray.add(secondSubcourseObj);
            }
            firstSubcourseObj.put("subcourses", secondSubcourseArray);
            mockSubcourseArray.add(firstSubcourseObj);
        }
        String mockSubcourseArrayStr = mockSubcourseArray.toJSONString();
        log.info("MOCK: " + mockSubcourseArrayStr);
        // May need change sessionKey to pass the test.
        ResponseUpdateCoursePages response = courseService.updateCoursePages(
                mockSubcourseArrayStr, course.getId(), teacher.getSessionKey());
        log.info(response.getErrMsg());
        assertTrue(response.isSuccess());
        String resSubcourseArrayStr = response.getPages();
        log.info("RES:" + resSubcourseArrayStr);
        JSONArray resSubcourseArray = JSONArray.parseArray(resSubcourseArrayStr);
        for (int i=0; i<resSubcourseArray.size(); i++) {
            JSONObject firstSubcourseObj = resSubcourseArray.getJSONObject(i);
            Integer subcourseId = firstSubcourseObj.getInteger("id");
            // Make sure first subcourse exists.
            assertNotNull(subcourseDAO.queryById(subcourseId));
            JSONArray secondSubcourseArray = firstSubcourseObj.getJSONArray("subcourses");
            for (int j=0; j<secondSubcourseArray.size(); j++) {
                JSONObject secondSubcourseObj = secondSubcourseArray.getJSONObject(j);
                subcourseId = secondSubcourseObj.getInteger("id");
                // Make sure second subcourse exists.
                assertNotNull(subcourseDAO.queryById(subcourseId));
            }
        }
    }

    @Test
    public void testPublishCourseNote() {
        // 发布公告
        RequestPublishCourseNote request = mockRequestPublishCourseNote();
        BaseResponse response = courseService.publishCourseNote(request);
        log.info("Message: " + response.getErrMsg());
        assertTrue(response.isSuccess());
        // 查询公告
        List<CourseNote> notes = courseNoteDAO.getAllNotes(request.getCourseId());
        assertNotNull(notes);
        assertEquals(notes.get(0).getCourseId(), (int)(request.getCourseId()));
    }

    @Test
    public void testUpdateCourseNote() {
        Course course = mockCourseWithTrueTeacher();
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());
        RequestPublishCourseNote publishRequest =
                mockRequestPublishCourseNoteWithTrueCourse(course,CourseNote.TYPE_BULLETIN);
        courseService.publishCourseNote(publishRequest);

        RequestUpdateCourseNote updateRequest = new RequestUpdateCourseNote();
        List<CourseNote> notes = courseNoteDAO.getAllNotes(course.getId());
        CourseNote note = notes.get(0);
        updateRequest.sessionKey = teacher.getSessionKey();
        updateRequest.courseNoteId = note.getId();
        updateRequest.type = note.getType();
        updateRequest.note = StringUtil.generateRandomString("Note: ");
        updateRequest.publishTime = note.getPublishTime().getTime();

        BaseResponse response = courseService.updateCourseNote(updateRequest);
        log.info("Message: " + response.getErrMsg());
        assertTrue(response.isSuccess());
        CourseNote newNote = courseNoteDAO.getNoteById(note.getId());
        assertFalse(note.getNote().equals(newNote.getNote()));
    }

    @Test
    public void testGetNotesWithSpecificType() {
        RequestPublishCourseNote request = mockRequestPublishCourseNoteWithSpecificType(CourseNote.TYPE_HOMEWORK);
        for (int i = 0; i < 5; ++i) {
            request.setNote(StringUtil.generateRandomString("Note: "));
            request.setPublishTime(new Date().getTime());
            courseService.publishCourseNote(request);
        }
        ResponseCourseNotes response = courseService.getNotesWithSpecificType(request.getCourseId(), CourseNote.TYPE_HOMEWORK);
        log.info("Message: " + response.getErrMsg());
        assertTrue(response.isSuccess());
        List<CourseNote> notes = response.getNotes();
        assertNotNull(notes);
        for (CourseNote note : notes) {
            assertEquals(note.getType(), CourseNote.TYPE_HOMEWORK);
        }
    }

    @Test
    public void testGetNotes() {
        RequestPublishCourseNote request = mockRequestPublishCourseNote();
        for (int i = 0; i < 5; ++i) {
            request.setNote(StringUtil.generateRandomString("Note: "));
            courseService.publishCourseNote(request);
        }
        ResponseCourseNotes response = courseService.getNotes(request.getCourseId());
        log.info("Message: " + response.getErrMsg());
        assertTrue(response.isSuccess());
        List<CourseNote> notes = response.getNotes();
        assertNotNull(notes);
        assertTrue(notes.size() >= 5);
    }

    private RequestPublishCourseNote mockRequestPublishCourseNote() {
        return mockRequestPublishCourseNoteWithSpecificType(new Random().nextInt(3) + 1);
    }

    private RequestPublishCourseNote mockRequestPublishCourseNoteWithSpecificType(Integer type) {
        return mockRequestPublishCourseNoteWithTrueCourse(null, type);
    }

    private RequestPublishCourseNote mockRequestPublishCourseNoteWithTrueCourse(Course course, Integer type)
            throws IllegalArgumentException {
        if (course == null) {
            course = mockCourseWithTrueTeacher();
        }
        RequestPublishCourseNote request = new RequestPublishCourseNote();
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());
        if (teacher == null) {
            throw new IllegalArgumentException("Can not find teacher!");
        }
        request.sessionKey = teacher.getSessionKey();
        request.courseId = course.getId();
        request.note = StringUtil.generateRandomString("Note: ");
        request.publishTime = new Date().getTime();

        if (type == null) {
            request.type = new Random().nextInt(3) + 1;
        } else {
            request.type = type;
        }
        log.info("CourseId: " + request.courseId + " " + request.getNote());
        return request;
    }

}
