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
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int fId;

	private String company;

	private String flightNo;

	private String departureCity;

	private String destinationCity;

	private String departureTime;

	private String destinationTime;

	private String totalTime;

	private double price;

	private String details;

	@ManyToMany(mappedBy = "flight", cascade = CascadeType.ALL)
	private List<Package> package1;

	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
	private List<FlightBook> flightBooks;

	public int getfId() {
		return fId;
	}

	public void setfId(int fId) {
		this.fId = fId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
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

	public List<FlightBook> getFlightBooks() {
		return flightBooks;
	}

	public void setFlightBooks(List<FlightBook> flightBooks) {
		this.flightBooks = flightBooks;
	}

}
