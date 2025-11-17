package com.chubb.FlightBookingSystem.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	boolean existsByFlight(Flight flight);
	boolean existsByDepartureDate(LocalDate departureDate);
}
