package com.mytrip.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.HotelRepository;
import com.mytrip.entities.Hotel;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	public Hotel AddHotel(Hotel hotel) {

		return hotelRepository.save(hotel);
	}

	public List<Hotel> GetAllHotel() {

		return hotelRepository.findAll();
	}

	public void deleteHotel(Integer hId) {
		hotelRepository.deleteById(hId);
	}
	
	public Hotel HotelByID(Integer hId) {
		Optional<Hotel> findById = hotelRepository.findById(hId);
		Hotel hotel = findById.get();
		return hotel;
	}
	
	public long HotelCOunt() {
		return hotelRepository.count();
	}

}
