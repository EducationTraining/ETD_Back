package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeacherDAO {
	Teacher queryById(int id);

	Teacher queryByUserName(String userName);

	Teacher queryBySessionKey(String sessionKey);

	boolean update(Teacher teacher);

	boolean create(Teacher teacher);

	/**
	 * 根据老师id删除该老师
	 * @param id
	 * @return
	 */
	boolean deleteTeacherById(int id);
}
