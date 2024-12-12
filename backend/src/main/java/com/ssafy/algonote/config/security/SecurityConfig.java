package com.ssafy.algonote.config.security;

import com.ssafy.algonote.config.jwt.JwtAuthFilter;
import com.ssafy.algonote.config.jwt.JwtUtil;
import com.ssafy.algonote.config.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/auth/**",
            "/member/**",
            "/problems/search-**",
            "/bookmarks/**",
            "/gpt/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/notifications/**"
    };

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList(
                    "http://localhost:3000",
                    "http://k10b203.p.ssafy.io:3000",
                    "https://algnote.duckdns.org",
                    "chrome-extension://jjejbifejcmbnabfpopimjabliklkfbg/",
                    "chrome-extension://nniojlfmhlicjobgkkhghjfmhocemhoj/",
                    "chrome-extension://ckiokomocdleklhfhlnnfgofidkejgco/"
            ));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 HTTP 메소드
            config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "token")); // 허용할 헤더
            config.setExposedHeaders(Arrays.asList("token")); // 클라이언트에 노출할 헤더
            config.setAllowCredentials(true);
            return config;
        }));


        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        http.formLogin(form -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new JwtAuthFilter(userDetailsService, jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint));


        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/member/update").authenticated()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}
