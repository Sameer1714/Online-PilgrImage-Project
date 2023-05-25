package com.mytrip.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

}
