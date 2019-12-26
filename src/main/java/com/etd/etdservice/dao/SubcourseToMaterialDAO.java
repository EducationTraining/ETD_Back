package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseMaterial;
import com.etd.etdservice.bean.course.Subcourse;
import com.etd.etdservice.bean.course.SubcourseToMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubcourseToMaterialDAO {
	/**
	 *
	 * @param subcourseToMaterial
	 * @return
	 */
	boolean create(SubcourseToMaterial subcourseToMaterial);

	/**
	 *
	 * @param subcourseId
	 * @return
	 */
	List<CourseMaterial> queryMaterialsBySubcourseId(int subcourseId);
}
