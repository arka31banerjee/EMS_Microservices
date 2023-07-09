package com.auth.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.management.entity.User;
import com.auth.management.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;

	public User saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		log.info("saveUser:{}",user.getUsername());
		return userRepository.save(user);
	}

	public void deleteUser(Long userId) {
		log.info("deleteUser:{}",userId);
		userRepository.deleteById(userId);
	}
	
	public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}