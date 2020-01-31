package com.etd.etdservice.service;


import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.CourseCategory;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.course.response.ResponseGetCoursesCategories;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.CourseCategoryDAO;
import com.etd.etdservice.dao.CourseCategoryDAOTest;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.AdminService;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.MD5Util;
import com.etd.etdservice.utils.StringUtil;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private CourseCategoryDAO courseCategoryDAO;

    @Test
    public void testRegister() {
        // 注册一条管理员记录
        ResponseRegister responseRegister;
        String name = StringUtil.generateRandomString("admin");
        String password = StringUtil.generateRandomString("password");
        // 调用service层方法
        responseRegister = adminService.register(name,password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(), MD5Util.getMD5String(name + password));
    }

    @Test
    public void testGetLoginInfo() {
        // 新建一条管理员记录
        ResponseRegister responseRegister;
        String name = StringUtil.generateRandomString("admin");
        String password = StringUtil.generateRandomString("password");
        adminService.register(name, password);
        // 调用service层的管理员登录方法
        responseRegister = adminService.getLoginInfo(name, password);
        assertTrue(responseRegister.isSuccess());
        assertEquals(responseRegister.getSessionKey(), MD5Util.getMD5String(name + password));
    }

    @Test
    public void testGetAdminInfo() {
        // 插入一条管理员记录
        ResponseGetAdmin responseGetAdmin;
        String userName = StringUtil.generateRandomString("admin");
        String password = StringUtil.generateRandomString("password");
        adminService.register(userName, password);
        // 调用service层方法
        responseGetAdmin = adminService.getAdminInfo(MD5Util.getMD5String(userName + password));
        assertEquals(userName, responseGetAdmin.getUserName());
    }

    @Test
    public void testGetAllCourses() {
        // 先清空课程表
        courseDAO.deleteAll();
        // 插入一条教师信息，并取得该教师的完整信息（包括id）
        Course course;
        Teacher teacher = new Teacher();
        String nameOfTeacher = StringUtil.generateRandomString("teacherUserName");
        String sessionKeyOfTeacher = StringUtil.generateRandomString("teacherSessionKey");
        teacher.setUserName(nameOfTeacher);
        teacher.setSessionKey(sessionKeyOfTeacher);
        teacher.setCreateTime(new Date());
        teacherDAO.create(teacher);
        teacher = teacherDAO.queryBySessionKey(sessionKeyOfTeacher);
        // 插入一条管理员信息，并取得该管理员的sessionKey
        String adminName = StringUtil.generateRandomString("adminName");
        String adminPassWord = StringUtil.generateRandomString("teacherPassword");
        String sessionKey = adminService.register(adminName, adminPassWord).getSessionKey();

        Integer MAX_VALUE = 100000000;
        int RANDOM_COURSE_NAME = new Random().nextInt(MAX_VALUE) + MAX_VALUE;
        // 插入10条有效课程信息
        for (int i = 0; i<10; i++) {
            course = new Course();
            // 每次的课程名称都不一样，避免无法插入，
            // 但是每次的课程名称都应该明确，不能完全随机
            course.setName("courseName" + (RANDOM_COURSE_NAME + i));
            course.setTeacherId(teacher.getId());
            course.setCreateTime(new Date());
            course.setTeacherId(teacher.getId());
            course.setCourseNum(StringUtil.generateRandomString("courseNum"));
            course.setAvatarUrl(StringUtil.generateRandomString("avatarUrl"));
            course.setCreateTime(new Date());
            course.setStartTime(new Date());
            course.setDescription(StringUtil.generateRandomString("description"));
            course.setNote(StringUtil.generateRandomString("note"));
            course.setPages(StringUtil.generateRandomString("pages"));
            course.setScore(DoubleUtil.nextDouble(0, 5));
            courseDAO.create(course);
        }
        // 插入一条无效课程信息
        course = new Course();
        int invalidTeacherId = new Random().nextInt(MAX_VALUE) + MAX_VALUE;
        course.setTeacherId(invalidTeacherId);
        course.setCourseNum(StringUtil.generateRandomString("courseNum"));
        course.setCreateTime(new Date());
        courseDAO.create(course);
        // 调用service层getAllCourses方法
        ResponseGetCourses courses = adminService.getAllCourses(sessionKey);
        // 判断返回结果是否符合预期
        assertTrue(courses.isSuccess());
        assertEquals(10, courses.getCoursesList().size());
        if (courses.getCoursesList().size() == 10) {
            for (int i = 0; i < 10; i++) {
                assertEquals("courseName" + String.valueOf(RANDOM_COURSE_NAME + i), courses.getCoursesList().get(i).getName());
            }
        }
        // 删除无效信息
        course = courseDAO.queryByCourseNum(course.getCourseNum());
        courseDAO.deleteByCourseId(course.getId());

        course = courseDAO.queryById(course.getId());
        Assert.assertNull(course);
    }

    @Test
    public void testUpdateCourseStatus() {
        // 插入一条管理员信息
        String adminName = StringUtil.generateRandomString("admin");
        String adminPassWord = StringUtil.generateRandomString("password");
        String sessionKeyOfAdmin = adminService.register(adminName, adminPassWord).getSessionKey();
        // 插入一条教师信息,并获得该教师的完整信息
        Teacher teacher = new Teacher();
        String nameOfTeacher = StringUtil.generateRandomString("teacher");
        String sessionKeyOfTeacher = StringUtil.generateRandomString("sessionKey");
        teacher.setUserName(nameOfTeacher);
        teacher.setSessionKey(sessionKeyOfTeacher);
        teacher.setCreateTime(new Date());
        teacherDAO.create(teacher);
        teacher = teacherDAO.queryBySessionKey(sessionKeyOfTeacher);
        // 插入一门课程信息，并获得该课程完整信息
        Course course = new Course();
        String nameOfCourse = StringUtil.generateRandomString("courseName");
        String coursesNum = StringUtil.generateRandomString("courseNum");
        course.setName(nameOfCourse);
        course.setTeacherId(teacher.getId());
        course.setCreateTime(new Date());
        course.setCourseNum(coursesNum);
        courseDAO.create(course);
        course = courseDAO.queryByCourseNum(coursesNum);
        // 调用service层方法，判断返回值是否符合预期
        assertTrue(adminService.updateCourseStatus(course.getId(),2, sessionKeyOfAdmin).isSuccess());
        assertTrue(adminService.updateCourseStatus(course.getId(),1, sessionKeyOfAdmin).isSuccess());
    }

    @Test
    public void updateCourseCategoryTest() {
        // 插入一条管理员信息
        String adminName = StringUtil.generateRandomString("admin");
        String adminPassWord = StringUtil.generateRandomString("password");
        String sessionKeyOfAdmin = adminService.register(adminName, adminPassWord).getSessionKey();
        // 增加一个课程类别
        CourseCategory courseCategory = CourseCategoryDAOTest.mockCourseCategory();
        int categoryId = courseCategory.getCategoryId();
        String categoryName = courseCategory.getCategoryName();
        adminService.addCourseCategory(categoryId, categoryName, sessionKeyOfAdmin);
        // 修改刚才增加的课程类别
        BaseResponse updateCategory = adminService.updateCourseCategory(categoryId, "updateCategory", sessionKeyOfAdmin);
        assertEquals("updateCategory", courseCategoryDAO.queryCategoryById(courseCategory.getCategoryId()).getCategoryName());
        assertEquals("", updateCategory.getErrMsg());
    }

    @Test
    public void getAllCoursesCategoriesTest() {
        // 插入一条管理员信息
        String adminName = StringUtil.generateRandomString("admin");
        String adminPassWord = StringUtil.generateRandomString("password");
        String sessionKeyOfAdmin = adminService.register(adminName, adminPassWord).getSessionKey();
        // 获取所有课程类别
        ResponseGetCoursesCategories allCoursesCategories = adminService.getAllCoursesCategories(sessionKeyOfAdmin);
        for (String category : allCoursesCategories.getCategoriesList()) {
            log.info(category);
        }
        assertEquals("", allCoursesCategories.getErrMsg());
    }

    @Test
    public void addCourseCategory() {
        // 插入一条管理员信息
        String adminName = StringUtil.generateRandomString("admin");
        String adminPassWord = StringUtil.generateRandomString("password");
        String sessionKeyOfAdmin = adminService.register(adminName, adminPassWord).getSessionKey();
        // 增加一个课程类别
        CourseCategory courseCategory = CourseCategoryDAOTest.mockCourseCategory();
        int categoryId = courseCategory.getCategoryId();
        String categoryName = courseCategory.getCategoryName();
        BaseResponse baseResponse = adminService.addCourseCategory(categoryId, categoryName, sessionKeyOfAdmin);
        assertEquals("", baseResponse.getErrMsg());
    }

    @Test
    public void deleteCourseCategory() {
        // 插入一条管理员信息
        String adminName = StringUtil.generateRandomString("admin");
        String adminPassWord = StringUtil.generateRandomString("password");
        String sessionKeyOfAdmin = adminService.register(adminName, adminPassWord).getSessionKey();
        // 增加一个课程类别
        CourseCategory courseCategory = CourseCategoryDAOTest.mockCourseCategory();
        int categoryId = courseCategory.getCategoryId();
        String categoryName = courseCategory.getCategoryName();
        adminService.addCourseCategory(categoryId, categoryName, sessionKeyOfAdmin);
        // 删除刚才新增的课程类别
        BaseResponse baseResponse = adminService.deleteCourseCategory(categoryId, sessionKeyOfAdmin);
        assertEquals("", baseResponse.getErrMsg());
    }

}
