package com.mytrip.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.HolidayPackageBook;
import com.mytrip.entities.User;

public interface HolidayBookRepository extends JpaRepository<HolidayPackageBook, Integer> {

	public HolidayPackageBook findByOrderId(String orderId);

	public List<HolidayPackageBook> findByStatus(String status);
	
	public List<HolidayPackageBook> findByUserAndStatus(User user, String status);

	public long countByStatus(String status);
}
