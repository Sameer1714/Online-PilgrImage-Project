package com.mytrip.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytrip.dao.PackageRepository;
import com.mytrip.entities.Package;

@Service
public class PackageService {

	@Autowired
	private PackageRepository packageRepository;

	public Package AddPackage(Package package1) {

		Package save = packageRepository.save(package1);

		return save;
	}

	public List<Package> getAllPAckage() {

		List<Package> list = packageRepository.findAll();

		return list;
	}

	public Package getSinglePackage(int id) {

		Optional<Package> findById = packageRepository.findById(id);

		Package package1 = findById.get();

		return package1;
	}

	public void DeletePackageById(Integer pId) {
		packageRepository.deleteById(pId);
	}
	
	public long PackageCount() {
		return packageRepository.count();
	}
}
