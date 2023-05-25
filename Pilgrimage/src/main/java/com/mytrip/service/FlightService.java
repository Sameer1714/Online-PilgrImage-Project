package com.mytrip.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.FlightRepository;
import com.mytrip.entities.Flight;

@Service
public class FlightService {

	@Autowired
	private FlightRepository flightRepository;

	public Flight AddFligh(Flight flight) {

		try {

			String departureTime = flight.getDepartureTime();
			String destinationTime = flight.getDestinationTime();

			SimpleDateFormat format = new SimpleDateFormat("hh:mm");

			Date date1 = format.parse(departureTime);

			Date date2 = format.parse(destinationTime);

			long difference = Math.abs(date2.getTime() - date1.getTime());

			long diffMinutes = difference / (60 * 1000) % 60;
			long diffHours = difference / (60 * 60 * 1000) % 24;

			String hour = String.valueOf(diffHours);

			String minute = String.valueOf(diffMinutes);

			flight.setTotalTime(hour + "h:" + minute + "m");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return flightRepository.save(flight);
	}

	public List<Flight> getAllFlight() {
		return flightRepository.findAll();
	}
	
	public void DeleteFlightById(Integer fId) {
		flightRepository.deleteById(fId);
	}
	
	public Flight getFlightById(Integer fId) {
		Optional<Flight> findById = flightRepository.findById(fId);
		Flight flight = findById.get();
		return flight;
	}
	
	public long  FlightCount() {
		 return flightRepository.count();
	}
}
