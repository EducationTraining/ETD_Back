package com.etd.etdservice.bean.course;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategory {
    private int id;
    private int categoryId;
    private String categoryName;
}
