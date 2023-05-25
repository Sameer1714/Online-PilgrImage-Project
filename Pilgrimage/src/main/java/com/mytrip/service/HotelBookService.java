package com.mytrip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.HotelBookRepository;
import com.mytrip.entities.HotelBook;
import com.mytrip.entities.User;

@Service
public class HotelBookService {

	@Autowired
	private HotelBookRepository hotelBookRepository;

	public HotelBook HotelBoook(HotelBook hotelBook) {
		return hotelBookRepository.save(hotelBook);
	}

	public HotelBook FindByOrderId(String orderId) {
		return hotelBookRepository.findByOrderId(orderId);
	}

	public List<HotelBook> GetAllBookHotel() {
		return hotelBookRepository.findByStatus("paid");
	}

	public long HotelBookingCOunt() {
		return hotelBookRepository.countByStatus("paid");
	}

	public List<HotelBook> UserBookingHotel(User user) {
		return hotelBookRepository.findByUserAndStatus(user, "paid");
	}
}
