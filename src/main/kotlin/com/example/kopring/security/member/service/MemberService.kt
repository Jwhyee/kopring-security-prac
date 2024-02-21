package com.example.kopring.security.member.service

import com.example.kopring.security.common.exception.InvalidInputException
import com.example.kopring.security.common.status.ROLE
import com.example.kopring.security.member.dto.MemberDtoRequest
import com.example.kopring.security.member.entity.Member
import com.example.kopring.security.member.entity.MemberRole
import com.example.kopring.security.member.repository.MemberRepository
import com.example.kopring.security.member.repository.MemberRoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberService(
   private val memberRepository: MemberRepository,
   private val memberRoleRepository: MemberRoleRepository
) {

   fun sighUp(dto: MemberDtoRequest): String {
      var member = memberRepository.findByLoginId(dto.loginId)
      if(member != null) throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")

      member = dto.toEntity()
      memberRepository.save(member)

      val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
      memberRoleRepository.save(memberRole)

      return "회원 가입이 완료되었습니다."
   }


}