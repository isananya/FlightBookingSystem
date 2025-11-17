package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Transactional
	public Schedule addSchedule(ScheduleRequestDTO scheduleDto) {
		String flightNumber = scheduleDto.getFlightNumber();
		
		Flight flight = flightRepository.findById(flightNumber)
		        .orElseThrow(() -> new FlightNotFoundException(flightNumber));
		
		Schedule schedule = new Schedule(scheduleDto,flight);
		
		if(scheduleRepository.existsByFlight(schedule.getFlight()) &&
				scheduleRepository.existsByDepartureDate(schedule.getDepartureDate())) {
			throw new ScheduleAlreadyExistsException(schedule.getFlight(), schedule.getDepartureDate());
		}
		return scheduleRepository.save(schedule);
	}
}
