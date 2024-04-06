package ru.otus.hw.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
			))
			.authorizeHttpRequests((requests) ->
				requests
					.requestMatchers("/", "/main").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin((formLogin) ->
				formLogin
					.usernameParameter("username")
					.passwordParameter("password")
					.loginPage("/authentication/login")
					.failureUrl("/authentication/login?failed")
					.permitAll()
			)
			.logout(LogoutConfigurer::permitAll);
		return http.build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
