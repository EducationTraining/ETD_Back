package com.etd.etdservice.service;


import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseUploadAvatar;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.CourseDAOTest;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.dao.UserDAOTest;
import com.etd.etdservice.serivce.CourseService;
import com.etd.etdservice.serivce.impl.CourseServiceImpl;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.FileHelper;
import com.etd.etdservice.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    private  CourseDAO courseDAO;

    private static TeacherDAO teacherDAO;

    private CourseDAOTest courseDAOTest;

    @Value("${image_root_path}")
    private String imageRootPath;

    @Value("${url_starter}")
    private String urlStarter;

    private static final int COURSE_NUM = 5;

    private static final int LIMIT = 3;

    private static final int MAX_PAGE = 10;

    @Autowired
    public  void setTeacherDAO(TeacherDAO teacherDAO) {
        CourseServiceTest.teacherDAO = teacherDAO;
    }

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

    @Test
    public void testGetHottestCourses(){
        for(int i = 0; i < COURSE_NUM; ++i){
            Course mockCourse = CourseServiceTest.mockCourse();
            courseDAO.create(mockCourse);
        }

        List<Course> courses = courseDAO.queryAllCourses();
        Course mockCourse = courses.get(0);
        mockCourse.setStatus(1);
        mockCourse.setScore(5.0);

        courseDAO.update(mockCourse);

        ResponseGetCourses responseGetCourses = courseService.getHottestCourses();
        Assert.assertTrue(responseGetCourses.isSuccess());
        Assert.assertNotNull(responseGetCourses.getCoursesList());

        boolean flag = false;
        List<ResponseCourse> courseList = responseGetCourses.getCoursesList();
        for(ResponseCourse course : courseList){
            if(course.getCourseNum().equals(mockCourse.getCourseNum())){
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

        System.out.println("-----------------------------------");
        System.out.println(responseUploadAvatar + "\n" + responseUploadAvatar.getErrMsg());
        System.out.println("-----------------------------------");

        Assert.assertNotNull(responseUploadAvatar);
        Assert.assertTrue(responseUploadAvatar.isSuccess());
        String expectedAvatarUrl =
                urlStarter + "/images/" + CourseServiceImpl.COURSE_TYPE + "/" + file.getOriginalFilename();
        Assert.assertEquals(responseUploadAvatar.getAvatarUrl(), expectedAvatarUrl);

        String filePath = imageRootPath + "/"+CourseServiceImpl.COURSE_TYPE+"/" + file.getOriginalFilename();
        File uploadedFile = new File(filePath);
        System.out.println("--------: " + uploadedFile.getPath() + "  -------");
        Assert.assertTrue(uploadedFile.exists());
    }

    @Test
    public void testUpdateCourseInfo(){
        Course mockCourse = mockCourse();
        Course course = mockCourse();
        mockCourse.setStatus(1);
        courseDAO.create(course);

        course = courseDAO.queryByCourseNum(course.getCourseNum());
        Teacher teacher = teacherDAO.queryById(course.getTeacherId());

        RequestUpdateCourse request = new RequestUpdateCourse();
        request.setSessionKey(teacher.getSessionKey());
        request.setCourseId(course.getId());
        request.setName(mockCourse.getName());
        request.setPages(mockCourse.getPages());
        request.setStartTime(mockCourse.getStartTime().getTime());
        request.setWeeks(mockCourse.getWeeks());
        request.setStatus(mockCourse.getStatus());
        request.setDescription(mockCourse.getDescription());
        request.setNote(mockCourse.getNote());
        request.setAvatarUrl(mockCourse.getAvatarUrl());
        request.setCourseId(course.getId());

        BaseResponse response = courseService.updateCourseInfo(request);
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
    }
}
