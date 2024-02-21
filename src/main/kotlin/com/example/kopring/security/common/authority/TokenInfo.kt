package com.example.kopring.security.common.authority

data class TokenInfo(
   // JWT 권한 인증 타입
   val grantType: String,
   // 실제 검증할 토큰
   val accessToken: String,
)
