package com.example.kopring.security.member.service

import com.example.kopring.security.common.authority.JwtTokenProvider
import com.example.kopring.security.common.authority.TokenInfo
import com.example.kopring.security.common.exception.InvalidInputException
import com.example.kopring.security.common.status.ROLE
import com.example.kopring.security.member.dto.LoginDto
import com.example.kopring.security.member.dto.MemberDtoRequest
import com.example.kopring.security.member.entity.Member
import com.example.kopring.security.member.entity.MemberRole
import com.example.kopring.security.member.repository.MemberRepository
import com.example.kopring.security.member.repository.MemberRoleRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberService(
   private val memberRepository: MemberRepository,
   private val memberRoleRepository: MemberRoleRepository,
   private val authenticationManagerBuilder: AuthenticationManagerBuilder,
   private val jwtTokenProvider: JwtTokenProvider
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

   /** 로그인 -> 토큰 발행 */
   fun login(loginDto: LoginDto): TokenInfo {
      // 사용자가 입력한 아이디, 비밀번호를 토대로 Token을 발행
      val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.longinId, loginDto.password)
      // 발행된 토큰을 ManagerBuilder에 전달
      // 이 때, DB에 있는 회원 정보와 비교를 함
      val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

      return jwtTokenProvider.createToken(authentication)
   }


}