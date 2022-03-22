package com.dh.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dh.security1.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findUserByUsername(String username);

	public User findByProviderId(String providerId);

}
