package com.etd.etdservice.dao;

import com.etd.etdservice.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO {
    /**
     *根据用户id给出具体用户信息
     *
     * @param id 用户id
     * @return User
     */
    User queryUserById(Integer id);
}
