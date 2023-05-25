package com.mytrip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.HolidayBookRepository;
import com.mytrip.entities.HolidayPackageBook;
import com.mytrip.entities.User;

@Service
public class PackageBookingService {

	@Autowired
	private HolidayBookRepository holidayBookRepository;

	public HolidayPackageBook BookPackage(HolidayPackageBook holidayPackageBook) {
		return holidayBookRepository.save(holidayPackageBook);
	}

	public HolidayPackageBook FindByOrderId(String orderId) {

		return holidayBookRepository.findByOrderId(orderId);
	}

	public List<HolidayPackageBook> GetBookingHoliday() {
		return holidayBookRepository.findByStatus("paid");
	}

	public long PackageBookingCount() {
		return holidayBookRepository.countByStatus("paid");
	}

	public List<HolidayPackageBook> UserBookingPackage(User user) {
		return holidayBookRepository.findByUserAndStatus(user, "paid");
	}
}
