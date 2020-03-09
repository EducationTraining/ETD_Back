package com.etd.etdservice.bean.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseNote {
    private int id;
    private int courseId;
    private String note;
    private Date publishTime;
    private Date createTime;
    private int type;

    /**
     * 公告
     */
    public static final int TYPE_BULLETIN = 1;

    /**
     * 测验
     */
    public static final int TYPE_TEST = 2;

    /**
     * 作业
     */
    public static final int TYPE_HOMEWORK = 2;

}
