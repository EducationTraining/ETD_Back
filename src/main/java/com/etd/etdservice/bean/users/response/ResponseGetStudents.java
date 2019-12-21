package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ResponseGetStudents extends BaseResponse {
    private List<ResponseGetStudent> studentsList;

    public ResponseGetStudents(boolean success, String errMsg, List<ResponseGetStudent> studentsList) {
           super(success, errMsg);
           this.studentsList = studentsList;
    }

}
