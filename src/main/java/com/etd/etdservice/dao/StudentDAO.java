package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentDAO {
	Student queryById(int id);

	Student queryByUserName(String userName);

	Student queryBySessionKey(String sessionKey);

	boolean update(Student student);

	boolean create(Student student);
}
