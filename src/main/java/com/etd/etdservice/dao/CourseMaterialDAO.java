package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CourseMaterialDAO {
	/**
	 *
	 * @param courseMaterial
	 * @return
	 */
	boolean create(CourseMaterial courseMaterial);
}
