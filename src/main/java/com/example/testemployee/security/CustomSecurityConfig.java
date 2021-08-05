package com.example.testemployee.security;

import com.example.testemployee.exceptions.GlobalExceptionHandler;
import com.example.testemployee.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MimeTypeUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;
    private final GlobalExceptionHandler globalExceptionHandler;

    public CustomSecurityConfig(ApplicationProperties applicationProperties,
                                GlobalExceptionHandler globalExceptionHandler) {
        this.applicationProperties = applicationProperties;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(applicationProperties.getAdminUsername())
                .password(passwordEncoder().encode(applicationProperties.getAdminPassword()))
                .roles("READ_WRITE");
        auth.inMemoryAuthentication().withUser(applicationProperties.getPublicUsername())
                .password(passwordEncoder().encode(applicationProperties.getPublicPassword()))
                .roles("READ_ONLY");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/v3/api-docs/public_**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    httpServletResponse.addHeader("content-type", MimeTypeUtils.APPLICATION_JSON_VALUE);
                    httpServletResponse.getOutputStream()
                            .write((new ObjectMapper()).writeValueAsBytes(
                                    globalExceptionHandler.handleAuthenticationException(e)));
                })
                .and().csrf().disable().exceptionHandling()
                .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    httpServletResponse.addHeader("content-type", MimeTypeUtils.APPLICATION_JSON_VALUE);
                    httpServletResponse.getOutputStream()
                            .write((new ObjectMapper()).writeValueAsBytes(
                                    globalExceptionHandler.handleAuthenticationException(e)));
                })
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    httpServletResponse.addHeader("content-type", MimeTypeUtils.APPLICATION_JSON_VALUE);
                    httpServletResponse.getOutputStream()
                            .write((new ObjectMapper()).writeValueAsBytes(
                                    globalExceptionHandler.handleAccessDeniedException(e)));
                });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}