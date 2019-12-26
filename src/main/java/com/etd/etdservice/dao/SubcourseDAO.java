package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.Subcourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SubcourseDAO {
	/**
	 *
	 * @param subcourse
	 * @return
	 */
	boolean create(Subcourse subcourse);

	/**
	 *
	 * @param subcourse
	 * @return
	 */
	boolean update(Subcourse subcourse);

	/**
	 *
	 * @param id
	 * @return
	 */
	Subcourse queryById(int id);
}
