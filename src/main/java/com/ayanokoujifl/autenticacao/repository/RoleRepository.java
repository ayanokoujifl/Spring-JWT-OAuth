package com.ayanokoujifl.autenticacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayanokoujifl.autenticacao.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);

}
