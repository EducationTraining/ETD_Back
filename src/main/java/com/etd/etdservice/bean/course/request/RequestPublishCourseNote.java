package com.etd.etdservice.bean.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPublishCourseNote {
    /**
     * 老师的sessionKey
     */
    public String sessionKey;

    /**
     * 课程id
     */
    public Integer courseId;

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
