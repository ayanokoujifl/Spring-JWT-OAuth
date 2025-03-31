package com.ayanokoujifl.autenticacao.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ayanokoujifl.autenticacao.domain.Role;
import com.ayanokoujifl.autenticacao.domain.User;
import com.ayanokoujifl.autenticacao.repository.RoleRepository;
import com.ayanokoujifl.autenticacao.repository.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
		var userAdmin = userRepository.findByUsername("admin");
		userAdmin.ifPresentOrElse(user -> {
			System.out.println("Admin já existe");
		}, () -> {
			User user = new User();

			user.setUsername("admin");
			user.setPassword(bCryptPasswordEncoder.encode("1718"));
			user.setRoles(Set.of(roleAdmin));
			
			userRepository.saveAndFlush(user);
		});
	}

}
