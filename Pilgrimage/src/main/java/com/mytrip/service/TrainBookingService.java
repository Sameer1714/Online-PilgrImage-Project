package com.mytrip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.TrainBookRepository;

import com.mytrip.entities.*;

@Service
public class TrainBookingService {

	@Autowired
	private TrainBookRepository trainBookRepository;

	public TrainBook TrainBook(TrainBook trainBook) {
		return trainBookRepository.save(trainBook);
	}

	public TrainBook FindByOrderId(String orderId) {

		return trainBookRepository.findByOrderId(orderId);
	}

	public List<TrainBook> getAllBookingTrain() {
		return trainBookRepository.findByStatus("paid");
	}

	public long TrainBookinhCOunt() {
		return trainBookRepository.countByStatus("paid");
	}

	public List<TrainBook> UserTrainBooking(User user) {
		return trainBookRepository.findByUserAndStatus(user, "paid");
	}
}
