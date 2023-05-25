package com.mytrip.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytrip.entities.Package;

public interface PackageRepository extends JpaRepository<Package, Integer> {

}
