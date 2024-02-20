package com.example.kopring.security.member.controller

import com.example.kopring.security.common.dto.BaseResponse
import com.example.kopring.security.member.dto.MemberDtoRequest
import com.example.kopring.security.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/member")
@RestController
class MemberController(
   private val memberService: MemberService
) {

   @PostMapping("/signup")
   fun signUp(@RequestBody @Valid dto: MemberDtoRequest): BaseResponse<Unit> {
      val resultMsg: String = memberService.sighUp(dto)
      return BaseResponse(message = resultMsg)
   }
}