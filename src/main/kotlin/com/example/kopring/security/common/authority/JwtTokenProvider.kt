package com.example.kopring.security.common.authority

import com.example.kopring.security.common.dto.CustomUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.Date

// 만료 시간 30분
const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30

@Component
class JwtTokenProvider {
   @Value("\${jwt.secret}")
   lateinit var secretKey: String

   private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

   /** Token 생성 */
   fun createToken(authentication: Authentication): TokenInfo {
      // 권한들을 쉼표 기준으로 나누고, 권한을 담음
      val authorities = authentication.authorities
         .joinToString(", ", transform = GrantedAuthority::getAuthority)

      val now = Date()
      val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

      // Access Token(현재 시간을 기준으로 권한들을 담아줌)
      val accessToken = Jwts.builder()
         .setSubject(authentication.name)
         .claim("auth", authorities)
         // 회원 확인을 위한 userId
         .claim("userId", (authentication.principal as CustomUser).userId)
         .setIssuedAt(now)
         .setExpiration(accessExpiration)
         .signWith(key, SignatureAlgorithm.HS256)
         .compact()

      return TokenInfo("Bearer", accessToken)
   }

   /** Token 정보 추출 */
   fun getAuthentication(accessToken: String): Authentication {
      // 토큰을 가지고 claim을 추출
      val claims = getClaims(accessToken)

      // auth에 대한 정보를 받아옴
      val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
      val userId = claims["userId"] ?: throw RuntimeException("잘못된 토큰입니다.")

      // 권한 정보 추출
      // String으로 변환한 뒤, 컴마를 기준으로 각 권한을 담아줌
      val authorities = (auth as String)
         .split(",")
         .map { SimpleGrantedAuthority(it) }

      val principal = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)

      return UsernamePasswordAuthenticationToken(principal, "", authorities)
   }

   private fun getClaims(accessToken: String): Claims = Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(accessToken)
      .body

   /** Token 검증 */
   fun validateToken(accessToken: String): Boolean {
      try {
         getClaims(accessToken)
         return true
      } catch (e: Exception) {
         when (e) {
            is SecurityException -> {
               // Invalid JWT Token
            }
            is MalformedJwtException -> {
               // Invalid JWT Token
            }
            is ExpiredJwtException -> {
               // Expired JWT Token
            }
            is UnsupportedJwtException -> {
               // Unsupported JWT Token
            }
            is IllegalArgumentException -> {
               // JWT claims string is empty
            }
            else -> {

            }
         }
         println(e.message)
      }
      return false
   }
}