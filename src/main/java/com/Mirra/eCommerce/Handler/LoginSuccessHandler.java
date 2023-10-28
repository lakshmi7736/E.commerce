package com.Mirra.eCommerce.Handler;

import java.io.IOException;
import java.util.Set;

import com.Mirra.eCommerce.Configure.CustomUser;
import com.Mirra.eCommerce.JwtSecurity.JwtHelper;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Token.RefreshToken;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.Token.RefreshTokenService;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {



	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserAdditionalService userAdditionalService;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		CustomUser userDetails = (CustomUser) authentication.getPrincipal();
		User user = userDetails.getUser();
		String email = userDetails.getUsername();

		if (!user.isEnable()) {
			request.getSession().setAttribute("lockedExceptionMessage", "Your account has been blocked.");
			response.sendRedirect("/signin?error");
			return;
		}

		System.out.println("bububuu");


		if (user.isAdminDelete()) {
			request.getSession().setAttribute("lockedExceptionMessage", "Your account has been removed.");
			response.sendRedirect("/signin?error");
			return;
		}
		System.out.println("bububuu");


		if (user.getFailedAttempt() > 0) {
			userAdditionalService.resetFailedAttempts(user.getEmail());
		}
		System.out.println("bububuu");


		UserDetails userDetails1 = userDetailsService.loadUserByUsername(email);
		String token = helper.generateToken(userDetails1);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

		JwtResponse response1 = JwtResponse.builder()
				.jwtToken(token)
				.username(userDetails1.getUsername())
				.name(user.getName())
				.refreshToken(refreshToken.getRefreshToken())
				.build();

		HttpSession session = request.getSession();
		session.setAttribute("jwtResponse", response1);

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		System.out.println("roles "+roles);
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/dashBoard");
		} else {
			response.sendRedirect("/");
		}
	}
}

