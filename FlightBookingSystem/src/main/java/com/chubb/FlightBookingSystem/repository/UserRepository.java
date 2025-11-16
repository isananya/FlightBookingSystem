package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	boolean existsByEmailId(String emailId);
}
