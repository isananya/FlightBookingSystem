package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>{

}
