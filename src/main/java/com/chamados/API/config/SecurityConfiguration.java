package com.chamados.API.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, SecurityFilterChain oauth2SecurityFilterChain) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer -> {
                    configurer
                            .loginPage("/login").permitAll()
                            .successHandler((request, response, authentication) -> {

                                if (authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority ->
                                                grantedAuthority.getAuthority().equals("ADMIN"))) {
                                    response.sendRedirect("/admin/dashboard");
                                }else {
                                    response.sendRedirect("/user/dashboard");
                                }
                            })
                            .failureUrl("/login?error");
                })

                .authorizeHttpRequests(authorize ->{
                    authorize.requestMatchers("/login/**").permitAll();
                    authorize.requestMatchers("icons/**").permitAll();
                    authorize.requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER");
                    authorize.requestMatchers("/admin/**").hasAuthority("ADMIN");
                    authorize.anyRequest().authenticated();
                })

                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/v2/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**"
        );
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

}
