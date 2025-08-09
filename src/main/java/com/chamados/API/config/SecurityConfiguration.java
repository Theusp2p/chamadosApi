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
                .formLogin(configurer -> configurer
                        .loginPage("/login").permitAll()
                        .successHandler((request, response, authentication) -> {
                            // Pega o usuário autenticado
                            Object principal = authentication.getPrincipal();
                            if (principal instanceof com.chamados.API.entities.User user) {
                                if (Boolean.TRUE.equals(user.getIsChangePasswordNextLogin())) {
                                    response.sendRedirect("/resetar-senha");
                                    return;
                                }

                                if (authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority ->
                                                grantedAuthority.getAuthority().equals("ADMIN"))) {
                                    response.sendRedirect("/admin/dashboard");
                                } else {
                                    response.sendRedirect("/user/dashboard");
                                }
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .failureUrl("/login?error")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/resetar-senha/**").permitAll()
                        .requestMatchers("/icons/**").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
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
