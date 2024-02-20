package com.example.kopring.security.member.service

import com.example.kopring.security.member.dto.MemberDtoRequest
import com.example.kopring.security.member.entity.Member
import com.example.kopring.security.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberService(
   private val memberRepository: MemberRepository
) {

   fun sighUp(dto: MemberDtoRequest): String {
      var member = memberRepository.findByLoginId(dto.loginId)
      if(member != null) return "이미 등록된 ID 입니다."
      member = Member(
         null,
         dto.loginId,
         dto.password,
         dto.name,
         dto.birthDate,
         dto.gender,
         dto.email
      )

      memberRepository.save(member)

      return "회원 가입이 완료되었습니다."
   }


}