package com.chubb.FlightBookingSystem.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.ScheduleNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.SeatNotAvailableException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	private double calculateTotalPrice(BookingRequestDTO request) {
	
		double departureFare = bookingRepository.getPrice(request.getDepartureScheduleId());
		double totalFare = departureFare;
		
		if(request.isRoundTrip() && request.getReturnScheduleId() != null){
			double returnFare = bookingRepository.getPrice(request.getReturnScheduleId());
			totalFare += returnFare;
		}
		
		return totalFare;
	}
	
	@Transactional
	public String addBooking(BookingRequestDTO request) {
		HashSet<TicketRequestDTO> passengers = request.getPassengers();
		for(TicketRequestDTO passenger: passengers) {
			if (scheduleRepository.isSeatBooked(request.getDepartureScheduleId(), passenger.getDepartureSeatNumber())>0) {
				throw new SeatNotAvailableException(passenger.getDepartureSeatNumber(), request.getDepartureScheduleId());
			}
			
			if(request.isRoundTrip() && scheduleRepository.isSeatBooked(request.getReturnScheduleId(), 
							passenger.getReturnSeatNumber())>0){
				throw new SeatNotAvailableException(passenger.getReturnSeatNumber(), request.getReturnScheduleId());
			}	
		}
		
		if(!scheduleRepository.existsById(request.getDepartureScheduleId())) {
			throw new ScheduleNotFoundException(request.getDepartureScheduleId());
		}
		
		if(request.isRoundTrip() && !scheduleRepository.existsById(request.getReturnScheduleId())) {
			throw new ScheduleNotFoundException(request.getReturnScheduleId());
		}
		
		Booking booking = new Booking(
				request.isRoundTrip(),
				request.getDepartureScheduleId(),
				request.getReturnScheduleId(),
				calculateTotalPrice(request),
				request.getEmailId(),
				request.getPassengerCount()	
		);
		
		bookingRepository.save(booking);
		
		Schedule departureSchedule = scheduleRepository.findById(request.getDepartureScheduleId());
		departureSchedule.setAvailableSeats(departureSchedule.getAvailableSeats()-request.getPassengerCount());
		
		
		
		Schedule returnSchedule = scheduleRepository.findById(request.getReturnScheduleId())
		        .orElseThrow(() -> new ScheduleNotFoundException(request.getReturnScheduleId()));
		returnSchedule.setAvailableSeats(returnSchedule.getAvailableSeats()-request.getPassengerCount());
	
		
		for(TicketRequestDTO passenger: passengers) {
			Ticket ticket = new Ticket(
					passenger.getFirstName(),
					passenger.getLastName(),
					passenger.getAge(),
					passenger.getGender(),
					passenger.getDepartureSeatNumber(),
					passenger.getMealOption(),
					booking
			);
			
			departureSchedule.getBookedSeats().add(passenger.getDepartureSeatNumber());
			
			ticketRepository.save(ticket);
			
			if(request.isRoundTrip() && request.getReturnScheduleId() != null) {
				ticket = new Ticket(
						passenger.getFirstName(),
						passenger.getLastName(),
						passenger.getAge(),
						passenger.getGender(),
						passenger.getReturnSeatNumber(),
						passenger.getMealOption(),
						booking
				);
				
				returnSchedule.getBookedSeats().add(passenger.getReturnSeatNumber());

				ticketRepository.save(ticket);
			}
		}
		
		scheduleRepository.save(departureSchedule);
		scheduleRepository.save(returnSchedule);
		
		return booking.getPnr();
		
	}
}
