package com.example.kopring.security.common.authority

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class JwtAuthenticationFilter(
   private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {


   override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
      val accessToken = resolveToken(request as HttpServletRequest)

      // accessToken이 정상적일 경우 인증 정보를 가져와 SecurityContextHolder에 정보를 저장
      if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
         val authentication = jwtTokenProvider.getAuthentication(accessToken)
         SecurityContextHolder.getContext().authentication = authentication
      }

      chain?.doFilter(request, response)
   }

   /** request header에 담긴 인증 정보를 받아 Bearer에 있는지 검증 후, 있다면 값을 뽑아옴 */
   private fun resolveToken(request: HttpServletRequest): String? {
      val bearerToken = request.getHeader("Authorization")
      return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
         bearerToken.substring(7)
      } else null
   }
}