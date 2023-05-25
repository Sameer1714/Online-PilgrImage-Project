package com.mytrip.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.BusBooking;
import com.mytrip.entities.BusBook;
import com.mytrip.entities.User;

@Service
public class BusBokkinService {

	@Autowired
	private BusBooking busBooking;

	public BusBook Busbook(BusBook busBook) {
		BusBook save = busBooking.save(busBook);

		return save;
	}

	public BusBook FIndBusById(Integer busId) {
		Optional<BusBook> findById = busBooking.findById(busId);
		BusBook busBook = findById.get();
		return busBook;
	}
	
	public BusBook findbyOrderId(String orderId) {
		BusBook findByOrderId = busBooking.findByOrderId(orderId);
		
		return findByOrderId;
	}
	
	public List<BusBook> GetAllBookingBusDetails() {
		List<BusBook> findByStatus = busBooking.findByStatus("paid");
		return findByStatus;
	}
	
	public long GetBookingCount() {
		return busBooking.countByStatus("paid");
	}
	
	public List<BusBook> BookingListOfUser(User user) {
		List<BusBook> findByUserAndStatusOrderByDateDesc = busBooking.findByUserAndStatus(user, "paid");
	return findByUserAndStatusOrderByDateDesc;	
	}
	
	
}
