package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeacherDAO {
	Teacher queryById(int id);

	Teacher queryByUserName(String userName);

	boolean update(Teacher teacher);

	boolean create(Teacher teacher);
}
