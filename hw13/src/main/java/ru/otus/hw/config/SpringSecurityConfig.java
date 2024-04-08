package ru.otus.hw.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((httpSecuritySessionManagementConfigurer ->
				httpSecuritySessionManagementConfigurer
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			)).authorizeHttpRequests((requests) -> requests
				.requestMatchers(HttpMethod.GET, "/books", "/api/v1/books/**",
					"/authors", "/api/v1/authors/**", "/api/v1/genres/**"
				).hasRole("USER")
				.requestMatchers("/books/**", "/api/v1/books/**")
				.hasAnyRole("ADMIN", "BOOK_MANAGER")
				.requestMatchers("/authors/**", "/api/v1/authors/**")
				.hasAnyRole("ADMIN", "AUTHOR_MANAGER")
				.anyRequest().authenticated()
			).formLogin((formLogin) -> formLogin
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/authentication/login")
				.failureUrl("/authentication/login?failed")
				.permitAll()
			).logout(LogoutConfigurer::permitAll);
		return http.build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
