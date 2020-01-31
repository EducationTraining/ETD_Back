package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseGetCoursesCategories extends BaseResponse {
    private List<String> categoriesList;

    public ResponseGetCoursesCategories(boolean success, String errMsg, List<String> categoriesList){
        super(success, errMsg);
        this.categoriesList = categoriesList;
    }
}
