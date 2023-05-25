package com.mytrip.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.BusRepository;
import com.mytrip.entities.Bus;

@Service
public class BusService {

	@Autowired
	private BusRepository busRepository;

	public Bus AddBus(Bus bus) {

		try {

			String departureTime = bus.getDepartureTime();
			String destinationTime = bus.getDestinationTime();

			SimpleDateFormat format = new SimpleDateFormat("hh:mm");

			Date date1 = format.parse(departureTime);

			Date date2 = format.parse(destinationTime);

			long difference = Math.abs(date2.getTime() - date1.getTime());

			long diffMinutes = difference / (60 * 1000) % 60;
			long diffHours = difference / (60 * 60 * 1000) % 24;

			String hour = String.valueOf(diffHours);

			String minute = String.valueOf(diffMinutes);

			bus.setTotalTime(hour + "h:" + minute + "m");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return busRepository.save(bus);
	}

	public List<Bus> GetAllBus() {
		return busRepository.findAll();
	}

	public void DeleteBusById(Integer bId) {
		busRepository.deleteById(bId);
	}

	public Bus GetBusById(Integer bId) {
		Optional<Bus> findById = busRepository.findById(bId);
		Bus bus = findById.get();
		return bus;
	}

	public long GetBusCount() {
		return busRepository.count();
	}

	public List<Bus> SearchingBus(String query) {
		List<Bus> findByDepartureCityOrDestinationCityContaining = busRepository
				.findByDepartureCityContaining(query);
		return findByDepartureCityOrDestinationCityContaining;
	}

}
