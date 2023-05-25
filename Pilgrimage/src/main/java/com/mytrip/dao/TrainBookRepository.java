package com.mytrip.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.TrainBook;
import com.mytrip.entities.User;

public interface TrainBookRepository extends JpaRepository<TrainBook, Integer> {

	public TrainBook findByOrderId(String orderId);
	
	public List<TrainBook> findByStatus(String status);
	
	public List<TrainBook> findByUserAndStatus(User user, String status);
	
	public long countByStatus(String status);
}
