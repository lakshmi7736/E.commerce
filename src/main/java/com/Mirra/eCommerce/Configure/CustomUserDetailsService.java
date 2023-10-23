package com.Mirra.eCommerce.Configure;

import com.Mirra.eCommerce.Exception.registration.UserNotFoundException;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UserNotFoundException("User not found with the provided username: " + username);
		}
		return new CustomUser(user);
	}


}
