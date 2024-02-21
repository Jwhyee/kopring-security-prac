package com.example.kopring.security.common.service

import com.example.kopring.security.member.entity.Member
import com.example.kopring.security.member.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
   private val memberRepository: MemberRepository,
   private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
   override fun loadUserByUsername(username: String): UserDetails {
      // 회원이 존재할 경우 User 인스턴스를 생성
      return memberRepository.findByLoginId(username)?.let { createUserDetails(it) }
         ?: throw UsernameNotFoundException("존재하지 않는 유저입니다.")
   }

   private fun createUserDetails(member: Member): UserDetails =
      User(
         member.loginId,
         passwordEncoder.encode(member.password),
         member.memberRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
      )
}