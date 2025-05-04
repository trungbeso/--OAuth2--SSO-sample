package com.trungbeso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.trungbeso.enums.Permission.*;
import static com.trungbeso.enums.Role.ADMIN;
import static com.trungbeso.enums.Role.MANAGER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private OAuth2LoginSuccessHandler loginSuccessHandler;

	//	@Value("${react.fontEnd-url}")
	private static final String fontEndUrl = "http://localhost:5173";

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			  .csrf(AbstractHttpConfigurer::disable)
			  .cors(cors -> cors.configurationSource(corsConfigurationSource()))
			  .authorizeHttpRequests(auth -> auth

				    // Config permission
					 .requestMatchers("/api/v1/manager/**").hasAnyRole(ADMIN.name(), MANAGER.name())
				    .requestMatchers(HttpMethod.GET, "/api/v1/manager/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name())
				    .requestMatchers(HttpMethod.POST, "/api/v1/manager/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
				    .requestMatchers(HttpMethod.PUT, "/api/v1/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
				    .requestMatchers(HttpMethod.DELETE, "/api/v1/manager/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
				    // --==================================-------
					 .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
				    .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
				    .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
				    .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
				    .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())

					 .anyRequest().permitAll())
			  .oauth2Login(oauth2 -> oauth2.successHandler(loginSuccessHandler));
		return http.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(fontEndUrl);
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
