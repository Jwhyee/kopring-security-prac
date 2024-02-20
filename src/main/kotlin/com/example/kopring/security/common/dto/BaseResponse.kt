package com.example.kopring.security.common.dto

import com.example.kopring.security.common.status.ResultCode

class BaseResponse<T>(
   val resultCode: String = ResultCode.SUCCESS.name,
   val data: T? = null,
   val message: String = ResultCode.SUCCESS.msg
) {

}