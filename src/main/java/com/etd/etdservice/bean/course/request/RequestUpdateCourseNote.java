package com.etd.etdservice.bean.course.request;

import java.util.Date;

public class    RequestUpdateCourseNote {
    /**
     * 老师的sessionKey
     */
    public String sessionKey;

    /**
     * 课程公告id
     */
    public Integer courseNoteId;

    /**
     * 公告内容
     */
    public String note;

    /**
     * 发布时间
     */
    public Long publishTime;

    /**
     * 公告类型:
     */
    public Integer type;
}
