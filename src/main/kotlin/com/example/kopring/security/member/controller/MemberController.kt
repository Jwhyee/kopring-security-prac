package com.example.kopring.security.member.controller

import com.example.kopring.security.member.dto.MemberDtoRequest
import com.example.kopring.security.member.service.MemberService
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
   fun signUp(@RequestBody dto: MemberDtoRequest): String {
      return memberService.sighUp(dto)
   }
}