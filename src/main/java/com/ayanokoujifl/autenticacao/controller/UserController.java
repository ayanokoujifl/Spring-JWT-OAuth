package com.ayanokoujifl.autenticacao.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ayanokoujifl.autenticacao.domain.Role;
import com.ayanokoujifl.autenticacao.domain.User;
import com.ayanokoujifl.autenticacao.dto.UserDto;
import com.ayanokoujifl.autenticacao.repository.RoleRepository;
import com.ayanokoujifl.autenticacao.repository.UserRepository;

import jakarta.transaction.Transactional;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/users")
	@Transactional
	public ResponseEntity<User> createUser(@RequestBody UserDto user) {

		Optional<User> obj = userRepository.findByUsername(user.username());
		if (obj.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} else {
			User newUser = new User();
			newUser.setUsername(user.username());
			newUser.setPassword(passwordEncoder.encode(user.password()));
			newUser.setRoles(Set.of(roleRepository.findByName(Role.Values.BASIC.name())));
			userRepository.save(newUser);
			return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
		}
	}

		@GetMapping("/users")
		@PreAuthorize("hasAuthority('SCOPE_admin')")
		public ResponseEntity<List<User>> getAllUsers() {
			List<User> users = userRepository.findAll();
			return ResponseEntity.ok(users);
		}

}
