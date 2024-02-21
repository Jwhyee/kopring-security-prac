package com.example.kopring.security.member.repository

import com.example.kopring.security.member.entity.Member
import com.example.kopring.security.member.entity.MemberRole
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
   fun findByLoginId(loginId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>