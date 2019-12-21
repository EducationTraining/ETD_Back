package com.etd.etdservice.bean.users.response;

import com.etd.etdservice.bean.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetAdmin extends BaseResponse {
	private String userName;
	private String phone;
	private String email;
}
