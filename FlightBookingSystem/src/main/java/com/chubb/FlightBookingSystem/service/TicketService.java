package com.chubb.FlightBookingSystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<TicketResponseDTO> getTicketsByPnr(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException(pnr));

        List<Ticket> tickets = ticketRepository.findByBooking(booking);

        List<TicketResponseDTO> response = tickets.stream()
            .map(t -> {
            	Schedule schedule = scheduleRepository.findById(t.getScheduleId());
                return new TicketResponseDTO(
                        t.getFirstName(),
                        t.getLastName(),
                        t.getAge(),
                        t.getGender(),
                        t.getSeatNumber(),
                        t.getMealOption(),
                        t.getStatus(),
                        schedule.getDepartureDate(),
                        schedule.getFlight().getSourceAirport(),
                        schedule.getFlight().getDestinationAirport(),
                        schedule.getFlight().getDepartureTime(),
                        schedule.getFlight().getArrivalTime()
                );
            }).toList();
        return response;
    }
    
    public List<TicketResponseDTO> getTicketsByEmail(String emailId) {
	    List<Booking> bookings = bookingRepository.findByEmailId(emailId);
	    if (bookings.isEmpty()) {
	        throw new BookingNotFoundException(emailId);
	    }

	    List<Ticket> tickets = new ArrayList<>();
	    for (Booking booking : bookings) {
	        tickets.addAll(ticketRepository.findByBooking(booking));
	    }

	    List<TicketResponseDTO> response = tickets.stream()
	        .map(t -> {
	            Schedule schedule = null;
	            
                schedule = scheduleRepository.findById(t.getScheduleId());

	            return new TicketResponseDTO(
	                t.getFirstName(),
	                t.getLastName(),
	                t.getAge(),
	                t.getGender(),
	                t.getSeatNumber(),
	                t.getMealOption(),
	                t.getStatus(),
	                schedule != null ? schedule.getDepartureDate() : null,
	                schedule != null ? schedule.getFlight().getSourceAirport() : null,
	                schedule != null ? schedule.getFlight().getDestinationAirport() : null,
	                schedule != null ? schedule.getFlight().getDepartureTime() : null,
	                schedule != null ? schedule.getFlight().getArrivalTime() : null
	            );
	        })
	        .toList();

	    return response;
	}
}