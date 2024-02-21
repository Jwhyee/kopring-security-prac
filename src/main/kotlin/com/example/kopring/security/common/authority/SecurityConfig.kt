package com.example.kopring.security.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
   private val jwtTokenProvider: JwtTokenProvider
) {

   @Bean
   fun filterChain(http: HttpSecurity): SecurityFilterChain {
      http
         .httpBasic { it.disable() }
         .csrf { it.disable() }
         // JWT를 사용하기 위해 세션을 사용하지 않도록 지정
         .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
         // 권한 관리를 넣어주는 부분
         .authorizeHttpRequests { req -> req
            .requestMatchers("/api/member/signup", "/h2-console/**").anonymous()
            .anyRequest().permitAll()
         }
         // 첫 필터를 성공할 경우, 뒤에 있는 Username.. 필터는 실행되지 않음
         .addFilterBefore(
            JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter::class.java
         )

      return http.build()
   }

   @Bean
   fun passwordEncoder(): PasswordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder()

}