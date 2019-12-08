package com.etd.etdservice.web;

import com.etd.etdservice.bean.User;
import com.etd.etdservice.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUserAcademyAssess(@PathVariable Integer id) {
        return service.getUserById(id);
    }
}
