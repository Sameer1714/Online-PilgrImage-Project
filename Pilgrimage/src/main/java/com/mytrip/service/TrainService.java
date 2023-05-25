package com.mytrip.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.TrainRepository;
import com.mytrip.entities.Train;

@Service
public class TrainService {

	@Autowired
	private TrainRepository trainRepository;

	public Train AddTrain(Train train) {

		try {

			String departureTime = train.getDepartureTime();
			String destinationTime = train.getDestinationTime();

			SimpleDateFormat format = new SimpleDateFormat("hh:mm");

			Date date1 = format.parse(departureTime);

			Date date2 = format.parse(destinationTime);

			long difference = Math.abs(date2.getTime() - date1.getTime());

			long diffMinutes = difference / (60 * 1000) % 60;
			long diffHours = difference / (60 * 60 * 1000) % 24;

			String hour = String.valueOf(diffHours);

			String minute = String.valueOf(diffMinutes);

			train.setTotalTime(hour + "h:" + minute + "m");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return trainRepository.save(train);
	}

	public List<Train> GetAllTrain() {
		return trainRepository.findAll();
	}
	
	public void DeleteTrainById(Integer tId) {
		trainRepository.deleteById(tId);
	}
	
	public Train getTrainById(Integer tId) {
		Optional<Train> findById = trainRepository.findById(tId);
		Train train = findById.get();	
		return train;
		
	}
	
	public long TrainCOunt() {
		return trainRepository.count();
	}

}
