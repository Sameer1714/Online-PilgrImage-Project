package com.mytrip.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.BusBook;
import com.mytrip.entities.User;

public interface BusBooking extends JpaRepository<BusBook, Integer> {

	public BusBook findByOrderId(String orderId);

	public List<BusBook> findByStatus(String status);

	public List<BusBook> findByUserAndStatus(User user, String status);

	public long countByStatus(String status);

}
