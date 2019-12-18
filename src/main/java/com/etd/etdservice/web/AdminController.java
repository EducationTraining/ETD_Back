package com.etd.etdservice.web;


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



}
