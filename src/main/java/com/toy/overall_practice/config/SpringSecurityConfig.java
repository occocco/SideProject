package com.toy.overall_practice.config;

import com.toy.overall_practice.jwt.JwtAuthenticationFilter;
import com.toy.overall_practice.jwt.handler.CustomAccessDeniedHandler;
import com.toy.overall_practice.jwt.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/join", "/logout", "/error", "/api/hello").permitAll()
                .antMatchers("/token/reissue").permitAll()
                .antMatchers(HttpMethod.POST, "/members").permitAll()
                .antMatchers("/members/**").hasAuthority("MEMBER")
                .antMatchers("/wallet/**").hasAuthority("MEMBER")
                .antMatchers("/categories/**","/regions").hasAuthority("MEMBER")
                .antMatchers("/tx/**").hasAuthority("MEMBER")
                .antMatchers("/**/*.ico", "/**/*.css", "/**/*.png", "/**/*.json", "/**/*.svg", "/**/*.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers("/**/*.ico", "/**/*.css", "/**/*.png", "/**/*.json", "/**/*.svg", "/**/*.js", "/error/**");
    }

}
