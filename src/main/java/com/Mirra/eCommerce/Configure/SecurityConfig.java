package com.Mirra.eCommerce.Configure;

import com.Mirra.eCommerce.Handler.LoginFailureHandler;
import com.Mirra.eCommerce.Handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;

	@Autowired
	public UserDetailsService userDetailsService;
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf(csrf->csrf.disable())
				.cors(cors->cors.disable())
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin((form) -> form
						.loginPage("/signin").loginProcessingUrl("/userLogin")
						.successHandler(loginSuccessHandler)
						.failureHandler(loginFailureHandler)
						.permitAll());
		return http.build();
	}

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable())
//				.cors(cors -> cors.disable())
//				.authorizeHttpRequests(requests -> requests
//						.requestMatchers("/user/**").hasRole("USER")
//						.requestMatchers("/admin/**").hasRole("ADMIN")
//						.requestMatchers("/**").permitAll()
//						.anyRequest().authenticated()
//				)
//				.formLogin(form -> form
//						.loginPage("/signin").loginProcessingUrl("/userLogin")
//						.successHandler(loginSuccessHandler)
//						.failureHandler(loginFailureHandler)
//						.permitAll()
//				)
//				.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}


}
