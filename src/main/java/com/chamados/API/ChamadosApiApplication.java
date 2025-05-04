package com.chamados.API;

import com.chamados.API.entities.User;
import com.chamados.API.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class ChamadosApiApplication {

	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || !auth.isAuthenticated()) {
				return Optional.empty();
			}
			return Optional.of(auth.getName()); // Retorna apenas o username
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ChamadosApiApplication.class, args);
	}

}
