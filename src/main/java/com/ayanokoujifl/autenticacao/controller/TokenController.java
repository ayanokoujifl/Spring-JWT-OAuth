package com.ayanokoujifl.autenticacao.controller;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ayanokoujifl.autenticacao.domain.Role;
import com.ayanokoujifl.autenticacao.domain.User;
import com.ayanokoujifl.autenticacao.dto.LoginRequest;
import com.ayanokoujifl.autenticacao.dto.LoginResponse;
import com.ayanokoujifl.autenticacao.repository.UserRepository;

@RestController
public class TokenController {

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		Optional<User> user = userRepository.findByUsername(request.username());
		if (user.isEmpty() || !user.get().isLoginCorrect(request, bCryptPasswordEncoder)) {
			throw new BadCredentialsException("username inválido");
		}

		var now = Instant.now();
		var expiresIn = 300L;

		var scopes = user.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

		var claims = JwtClaimsSet.builder().issuer("jwtAuth").issuedAt(now).subject(user.get().getId().toString())
				.expiresAt(now.plusSeconds(expiresIn)).claim("scope", scopes).build();

		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		System.out.println(scopes);
		return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
	}
}
