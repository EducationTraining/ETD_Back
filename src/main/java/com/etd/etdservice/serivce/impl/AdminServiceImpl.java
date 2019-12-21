package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.Admin;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.AdminDAO;
import com.etd.etdservice.dao.CourseDAO;
import com.etd.etdservice.dao.CourseStudentDAO;
import com.etd.etdservice.dao.TeacherDAO;
import com.etd.etdservice.serivce.AdminService;
import com.etd.etdservice.utils.MD5Util;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private TeacherDAO teacherDAO;
    @Autowired
    private CourseStudentDAO courseStudentDAO;

    /**
     * 管理员获取所有课程
     * @param sessionKey 管理员sessionKey
     * @return
     */
    @Override
    public ResponseGetCourses getAllCourses(String sessionKey) {
        // 如果sessionKey为null，则直接返回失败
        if (sessionKey == null) {
            return new ResponseGetCourses(false, "sessionKey error", null);
        }
        // 如果该sessionKey不存在，返回失败：没有管理员权限
        Admin resAdmin = adminDAO.queryBySessionKey(sessionKey);
        if (resAdmin == null) {
            return new ResponseGetCourses(false, "Admin not found !", null);
        }
        // 获取所有课程
        Page<Course> courses = courseDAO.queryAllCourses();
        List<ResponseCourse> coursesList = new ArrayList<>();
        for (Course course : courses) {
            // 获取每门课程的教师信息
            Teacher teacher = teacherDAO.queryById(course.getTeacherId());
            // 若教师信息不存在，则此门课程无效，丢弃该课程
            if (teacher == null) {
                continue;
            }
            ResponseGetTeacher responseGetTeacher = ResponseGetTeacher.fromBeanToResponse(teacher);
            // 获取每门课的学生数量
            int num_stu = courseStudentDAO.getStudentCountsByCourseId(course.getId());
            ResponseCourse responseCourse = ResponseCourse.fromBeanToResponse(course, responseGetTeacher, num_stu);
            coursesList.add(responseCourse);
        }
        if (coursesList.size() == 0) {
            return new ResponseGetCourses(false, "当前没有课程",coursesList);
        }
        return new ResponseGetCourses(true, "", coursesList);
    }

    @Override
    public BaseResponse updateCourseStatus(Integer courseId, Integer status, String sessionKey) {
        BaseResponse baseResponse = new BaseResponse();
        // 如果session为null，返回失败
        if (sessionKey == null) {
            baseResponse.setFailResponse(BaseResponse.NULL_SESSION_KEY);
            return baseResponse;
        }
        // 如果该sesionKey不存在，返回失败：没有管理员权限
        Admin resAdmin = adminDAO.queryBySessionKey(sessionKey);
        if (resAdmin == null) {
            baseResponse.setFailResponse(BaseResponse.NULL_ADMIN);
            return baseResponse;
        }
        // 获取该课程，并修改状态
        Course course = courseDAO.queryById(courseId);
        course.setStatus(status);
        if (courseDAO.update(course)) {
            //修改成功，返回成功
            baseResponse.setSuccess(true);
            return baseResponse;
        }
        //修改失败，返回失败
        baseResponse.setSuccess(false);
        baseResponse.setErrMsg("error !");
        return baseResponse;
    }

    @Override
    public ResponseGetAdmin getAdminInfo(String sessionKey) {
        ResponseGetAdmin responseGetAdmin=new ResponseGetAdmin();
        // 若sessionKey为null或者sessionKey不存在，则返回null
        if (sessionKey == null) {
            responseGetAdmin.setFailResponse(BaseResponse.NULL_SESSION_KEY);
            return responseGetAdmin;
        }
        Admin admin = adminDAO.queryBySessionKey(sessionKey);
        if (admin == null) {
            responseGetAdmin.setFailResponse(BaseResponse.NULL_ADMIN);
            return responseGetAdmin;
        }
        // 封装管理员信息并返回
        responseGetAdmin.setSuccess(true);
        responseGetAdmin.setUserName(admin.getUserName());
        responseGetAdmin.setEmail(admin.getEmail());
        responseGetAdmin.setPhone(admin.getPhone());
        return responseGetAdmin;
    }

    @Override
    public ResponseRegister register(String userName, String password) {
        // 若用户名或密码为null，返回失败
        if (userName == null || password == null) {
            return new ResponseRegister(false, "param error", null);
        }
        // 若该用户名已存在，返回失败
        Admin resAdmin = adminDAO.queryByUserName(userName);
        if (resAdmin != null) {
            return new ResponseRegister(false, "invalid userName", null);
        }
        // 封装Admin对象并插入表中
        Admin registerAdmin = new Admin();
        String sessionKey = MD5Util.getMD5String(userName + password);
        registerAdmin.setSessionKey(sessionKey);
        registerAdmin.setUserName(userName);
        registerAdmin.setCreateTime(new Date());
        // 插入失败，返回失败
        if (!adminDAO.create(registerAdmin)) {
            return new ResponseRegister(false, "cannot create student", null);
        }
        // 插入成功，返回成功
        return new ResponseRegister(true, "", sessionKey);
    }

    @Override
    public ResponseRegister getLoginInfo(String userName, String password) {
        // 若用户名或密码为null，返回失败
        if (userName == null || password == null) {
            return new ResponseRegister(false, "param error", null);
        }
        // 若该用户名不存在，返回失败
        Admin resAdmin = adminDAO.queryByUserName(userName);
        if (resAdmin == null) {
            return new ResponseRegister(false, "invalid user name, may need register", null);
        }
        // 获取管理员信息
        String resSessionKey = resAdmin.getSessionKey();
        // 验证用户名和密码是否正确，正确则返回正确
        if (resSessionKey.equals(MD5Util.getMD5String(userName + password))) {
            return new ResponseRegister(true, "",  resSessionKey);
        }
        // 用户名和密码不正确
        else {
            return new ResponseRegister(false, "invalid password", null);
        }
    }
}
