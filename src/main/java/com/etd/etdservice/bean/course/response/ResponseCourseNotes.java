package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.CourseNote;
import lombok.Data;

import java.util.List;

@Data
public class ResponseCourseNotes extends BaseResponse {
    private List<CourseNote> notes;

    public ResponseCourseNotes(boolean success, String errMsg, List<CourseNote> notes) {
        super(success, errMsg);
        this.notes = notes;
    }
}
