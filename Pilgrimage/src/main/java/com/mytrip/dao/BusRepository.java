package com.mytrip.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.Bus;

public interface BusRepository extends JpaRepository<Bus, Integer> {
	
	
	 public List<Bus> findByDepartureCityContaining(String departureCity);

}
