package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseCategoryDAO {

    List<String> queryAllCategoryName();

    boolean deleteCourseCategory(int categoryId);

    boolean addCourseCategory(CourseCategory courseCategory);

    int queryCategoryIdByName(String categoryName);

    boolean updateCategoryName(int categoryId, String categoryName);

    CourseCategory queryCategoryById(int categoryId);
}
