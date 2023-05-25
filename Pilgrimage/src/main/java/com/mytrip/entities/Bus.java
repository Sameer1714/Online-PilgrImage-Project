package com.mytrip.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Bus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bId;

	private String company;

	private String busNo;

	private String departureCity;

	private String destinationCity;

	private String departureTime;

	private String destinationTime;

	private double price;

	private String details;

	private String totalTime;

	@ManyToMany(mappedBy = "bus", cascade = CascadeType.ALL)
	private List<Package> package1;

	@OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
	private List<BusBook> busBooks;

	public int getbId() {
		return bId;
	}

	public void setbId(int bId) {
		this.bId = bId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getDestinationTime() {
		return destinationTime;
	}

	public void setDestinationTime(String destinationTime) {
		this.destinationTime = destinationTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public List<Package> getPackage1() {
		return package1;
	}

	public void setPackage1(List<Package> package1) {
		this.package1 = package1;
	}

	public List<BusBook> getBusBooks() {
		return busBooks;
	}

	public void setBusBooks(List<BusBook> busBooks) {
		this.busBooks = busBooks;
	}

}
