package com.etd.etdservice.serivce.impl;

import com.etd.etdservice.bean.User;
import com.etd.etdservice.dao.UserDAO;
import com.etd.etdservice.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUserById(Integer id) {
        //不做空之类的判断, 业务逻辑中要考虑得更多！
        return userDAO.queryUserById(id);
    }
}
