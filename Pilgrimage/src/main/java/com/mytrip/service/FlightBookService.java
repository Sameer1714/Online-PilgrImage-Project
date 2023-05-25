package com.mytrip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.stereotype.Service;

import com.mytrip.dao.FlightBookRepository;

import com.mytrip.entities.*;

@Service
public class FlightBookService {

	@Autowired
	private FlightBookRepository flightBookRepository;

	public FlightBook FlightBook(FlightBook flightBook) {
		return flightBookRepository.save(flightBook);
	}

	public FlightBook FindByOrderId(String orderId) {
		return flightBookRepository.findByOrderId(orderId);

	}

	public List<FlightBook> getAllFlightBooking() {
		return flightBookRepository.findByStatus("paid");
	}

	public long FlightBookingCount() {
		return flightBookRepository.countByStatus("paid");
	}

	public List<FlightBook> UserFlightBooking(User user) {
		return flightBookRepository.findByUserAndStatus(user, "paid");
	}
}
