package com.chubb.FlightBookingSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private String pnr;
	
	private boolean roundTrip;
	
	private int departureScheduleId;
	
	private int arrivalScheduleId;
	
	@Email
	private String emailId;
	
	@Min(value=1)
	private int ticketCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	public int getDepartureScheduleId() {
		return departureScheduleId;
	}

	public void setDepartureScheduleId(int departureScheduleId) {
		this.departureScheduleId = departureScheduleId;
	}

	public int getArrivalScheduleId() {
		return arrivalScheduleId;
	}

	public void setArrivalScheduleId(int arrivalScheduleId) {
		this.arrivalScheduleId = arrivalScheduleId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}
}
