package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.Admin;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseGetTeacher;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.dao.*;
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

    @Override
    public ResponseGetCourses getAllCourses(String sessionKey) {
        if (sessionKey == null) {
            return new ResponseGetCourses(false, "sessionKey error", null);
        }
        Admin resAdmin=adminDAO.queryBySessionKey(sessionKey);
        if(resAdmin==null){
            return new ResponseGetCourses(false,"Admin not found !",null);
        }
        Page<Course> courses = courseDAO.queryAllCourses();
        List<ResponseCourse> coursesList=new ArrayList<ResponseCourse>();
        for(Course course :courses)
        {
            Teacher teacher=teacherDAO.queryById(course.getId());
            ResponseGetTeacher responseGetTeacher=ResponseGetTeacher.fromBeanToResponse(teacher);
            int num_stu=courseStudentDAO.getStudentCountsByCourseId(course.getId());
            ResponseCourse responseCourse=ResponseCourse.fromBeanToResponse(course,responseGetTeacher,num_stu);
            coursesList.add(responseCourse);
        }
        return new ResponseGetCourses(true,"",coursesList);
    }

    @Override
    public BaseResponse updateCourseStatus(Integer courseId, Integer status, String sessionKey) {
        BaseResponse baseResponse=new BaseResponse();
        if (sessionKey == null) {
            baseResponse.setSuccess(false);
            baseResponse.setErrMsg("sessionKey error !");
            return baseResponse;
        }
        Admin resAdmin=adminDAO.queryBySessionKey(sessionKey);
        if(resAdmin==null){
            baseResponse.setSuccess(false);
            baseResponse.setErrMsg("Admin not found !");
            return baseResponse;
        }
        Course course=courseDAO.queryById(courseId);
        course.setStatus(status);
        if(courseDAO.update(course))
        {
            baseResponse.setSuccess(true);
            return baseResponse;
        }
        baseResponse.setSuccess(false);
        baseResponse.setErrMsg("error !");
        return baseResponse;
    }

    @Override
    public ResponseGetAdmin getAdminInfo(String sessionKey) {
        if(sessionKey==null)
            return null;
        Admin admin=adminDAO.queryBySessionKey(sessionKey);
        if(admin==null)
            return null;
        ResponseGetAdmin responseGetAdmin=new ResponseGetAdmin();
        responseGetAdmin.setUserName(admin.getUserName());
        responseGetAdmin.setEmail(admin.getEmail());
        responseGetAdmin.setPhone(admin.getPhone());
        return responseGetAdmin;
    }

    @Override
    public ResponseRegister register(String userName, String password) {

        if (userName == null || password == null) {
            return new ResponseRegister(false, "param error", null);
        }

        Admin resAdmin  = adminDAO.queryByUserName(userName);
        if (resAdmin != null) {
            return new ResponseRegister(false, "invalid userName", null);
        }
        Admin registerAdmin = new Admin();
        String sessionKey = MD5Util.getMD5String(userName + password);
        registerAdmin.setSessionKey(sessionKey);
        registerAdmin.setUserName(userName);
        registerAdmin.setCreateTime(new Date());
        if (!adminDAO.create(registerAdmin)) {
            return new ResponseRegister(false, "cannot create student", null);
        }

        return new ResponseRegister(true, "", sessionKey);
    }

    @Override
    public ResponseRegister getLoginInfo(String userName, String password) {

        if (userName == null || password == null) {
            return new ResponseRegister(false, "param error", null);
        }

        Admin resAdmin = adminDAO.queryByUserName(userName);
        if (resAdmin == null) {
            return new ResponseRegister(false,
                    "invalid user name, may need register", null);
        } else {
            String resSessionKey = resAdmin.getSessionKey();
            if (resSessionKey.equals(MD5Util.getMD5String(userName + password))) {
                return new ResponseRegister(true, "",  resSessionKey);
            } else {
                return new ResponseRegister(false,
                        "invalid password", null);
            }
        }
    }
}
