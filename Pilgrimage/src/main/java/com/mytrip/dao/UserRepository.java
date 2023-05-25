package com.mytrip.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mytrip.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

}
