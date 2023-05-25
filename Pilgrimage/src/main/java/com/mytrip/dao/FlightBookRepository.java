package com.mytrip.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.FlightBook;
import com.mytrip.entities.User;

public interface FlightBookRepository extends JpaRepository<FlightBook, Integer> {

	public FlightBook findByOrderId(String orderId);

	public List<FlightBook> findByStatus(String status);
	
	public List<FlightBook> findByUserAndStatus(User user, String status);
	
	public long countByStatus(String status);
}
