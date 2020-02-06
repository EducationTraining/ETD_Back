package com.etd.etdservice.bean.users.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateStudentByAdmin {
    private int id;//学生的id
    private String adminSessionKey;//管理员的sessionKey
    private boolean valid;//学生的有效性
}
