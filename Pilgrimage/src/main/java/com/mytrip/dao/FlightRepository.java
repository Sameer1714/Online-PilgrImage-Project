package com.mytrip.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer> {

}
