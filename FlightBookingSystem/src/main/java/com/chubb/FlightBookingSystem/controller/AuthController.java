package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.service.UserService;

import jakarta.validation.Valid;

@RestController
public class AuthController {
	@Autowired
	UserService userService;
	
	@PostMapping("/signup")
	User saveUser(@RequestBody @Valid User user) {
		userService.insertUser(user);
		return user;
	}
	

}
