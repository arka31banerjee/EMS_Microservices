package com.auth.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.management.dto.UserRequest;
import com.auth.management.entity.User;
import com.auth.management.service.UserAuthenticationService;

@RestController
@RequestMapping("/user")
public class UserAuthController {

	@Autowired
	private UserAuthenticationService authService;

	@Autowired
	private AuthenticationManager authmanager;

	@PostMapping("/register")
	public User saveUser(@RequestBody User user) {
		return authService.saveUser(user);
	}
	
	@PostMapping("/token")
	public String getToekn(@RequestBody UserRequest userReq) {
		Authentication authenticate = authmanager.authenticate(new UsernamePasswordAuthenticationToken(userReq.getUsername(), userReq.getPassword()));
		if(authenticate.isAuthenticated())
			return authService.generateToken(userReq.getUsername());
		else
			return "User not valid";
		
	}
	@GetMapping("/validate")
	public String validateToekn(@RequestParam("token")String userToken) {
		try{
			authService.validateToken(userToken);
			return "Valid token provided";
		}catch(Exception ex) {
			return "Invalid token provided";
		}
		
	}

}
