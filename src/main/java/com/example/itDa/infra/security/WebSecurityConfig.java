package com.example.itDa.infra.security;


import com.example.itDa.infra.security.filter.JwtAuthenticationFilter;
import com.example.itDa.infra.security.filter.JwtAuthorizationFilter;
import com.example.itDa.infra.security.handler.AccessDeniedHandler;
import com.example.itDa.infra.security.handler.AuthenticationFailureHandler;
import com.example.itDa.infra.security.handler.AuthenticationSuccessHandler;
import com.example.itDa.infra.security.handler.AuthorizationFailureHandler;
import com.example.itDa.infra.security.jwt.HeaderTokenExtractor;
import com.example.itDa.infra.security.provider.JwtAuthenticationProvider;
import com.example.itDa.infra.security.provider.JwtAuthorizationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured ??????????????? ?????????
public class WebSecurityConfig {

    private final JwtAuthorizationProvider jwtAuthorizationProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthorizationFailureHandler authorizationFailureHandler;



    @Bean // ???????????? ?????????
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean // ???????????? ????????? ????????????
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagger/**",
                        "/h2-console/**",
                        "/stomp/chat/**",
                        "/actuator/**"
        );

    }

    /*
     http security: cross site request forgery ???????????? ?????? ?????? ??????
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {

        // ?????? (Authentication)**: ????????? ????????? ???????????? ??????
        // ?????? (Authorization)**: ????????? ????????? ???????????? ??????
        auth
                .authenticationProvider(jwtAuthorizationProvider)
                .authenticationProvider(jwtAuthenticationProvider());

        http.csrf().disable();
        http.formLogin().disable();
        http.cors().configurationSource(corsConfigurationSource());


        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // ?????? ??????, ????????????
        http
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //???????????? ?????? ?????????
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration));
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");

        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jwtAuthenticationFilter.afterPropertiesSet();

        return jwtAuthenticationFilter;
    }

    //???????????? ?????? ?????????
    private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {

        List<String> skipPathList = new ArrayList<>();

        // ?????? ?????? API SKIP ??????
        skipPathList.add("POST,/api/users/signup");
        skipPathList.add("POST,/api/users/email");
        skipPathList.add("GET,/users/login/**");



        //WebSocket ?????? -> Filter ?????? Intercepter??? ?????????.
        skipPathList.add("GET,/stomp/chat/**");
        skipPathList.add("GET,/chat/**");


        //?????? ????????? ??????
        skipPathList.add("GET,/");
        skipPathList.add("GET,/favicon.ico");
        skipPathList.add("GET,/error");

        FilterSkipMatcher matcher = new FilterSkipMatcher(skipPathList, "/**");
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(headerTokenExtractor, matcher);

        filter.setAuthenticationFailureHandler(authorizationFailureHandler);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(encodePassword());
    }


    @Bean // cors ?????? ??????
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("https://it-da.shop");
        configuration.addAllowedOrigin("https://cheoljun.shop");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
