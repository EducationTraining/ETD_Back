package com.etd.etdservice.web;


import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.request.RequestUpdateCourse;
import com.etd.etdservice.bean.course.response.ResponseGetCourses;
import com.etd.etdservice.bean.users.requests.RequestRegister;
import com.etd.etdservice.bean.users.response.ResponseGetAdmin;
import com.etd.etdservice.bean.users.response.ResponseRegister;
import com.etd.etdservice.serivce.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService service;

    @RequestMapping(value = "/register/admin", method = RequestMethod.POST)
    public ResponseRegister registerForAdmin(@RequestBody RequestRegister request) {
        String password = request.getPassword();
        String userName = request.getUserName();
        return service.register(userName, password);
    }

    @RequestMapping(value = "/session-key/{sessionKey}/admin", method = RequestMethod.GET)
    public ResponseGetAdmin getAdminInfo(@PathVariable String sessionKey) {
        return service.getAdminInfo(sessionKey);
    }

    @RequestMapping(value = "/login/admin", method = RequestMethod.POST)
    public ResponseRegister getAdminLoginInfo(@RequestBody RequestRegister request) {
        String password = request.getPassword();
        String userName = request.getUserName();
        return service.getLoginInfo(userName, password);
    }

    @RequestMapping(value = "/session-key/{sessionKey}/admin/getCourses", method = RequestMethod.POST)
    public ResponseGetCourses getAllCourses(@PathVariable String sessionKey) {
        return service.getAllCourses(sessionKey);
    }



    @RequestMapping(value = "/update/course", method = RequestMethod.POST)
    public BaseResponse updateCourse(@RequestBody RequestUpdateCourse requestUpdateCourse) {
        Integer courseId = requestUpdateCourse.getCourseId();
        Integer status = requestUpdateCourse.getStatus();
        String sessionKey = requestUpdateCourse.getSessionKey();
        return service.updateCourseStatus(courseId, status, sessionKey);
    }

}
