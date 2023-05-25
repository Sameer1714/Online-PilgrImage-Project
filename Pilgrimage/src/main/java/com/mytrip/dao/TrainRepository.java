package com.mytrip.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.Train;

public interface TrainRepository extends JpaRepository<Train, Integer> {

}
