package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>{
	@Query("SELECT s.basePrice FROM Schedule s WHERE s.id = :scheduleId")
	double getPrice(@Param("scheduleId") int scheduleId);
}
