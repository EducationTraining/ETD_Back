package com.etd.etdservice.bean.course.response;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.CourseMaterial;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor
@Data
public class ResponseGetMaterials extends BaseResponse {
    List<CourseMaterial> courseMaterials;

    public ResponseGetMaterials(boolean success, String errMsg, List<CourseMaterial> courseMaterials) {
        super(success, errMsg);
        this.courseMaterials = courseMaterials;
    }
}
