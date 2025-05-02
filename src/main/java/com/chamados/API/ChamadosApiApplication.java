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
		public AuditorAware<User> auditorAware(UserService userService) {
			return () -> {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				if (auth == null || !auth.isAuthenticated()) {
					return Optional.empty();
				}

				// Reaproveita a mesma lógica do AuthenticationProvider!
				String username = auth.getName();
				return Optional.ofNullable(userService.getByUsername(username));
			};
		}

	public static void main(String[] args) {
		SpringApplication.run(ChamadosApiApplication.class, args);
	}

}
