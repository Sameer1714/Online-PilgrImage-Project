package com.mytrip.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Package {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int pId;

	private String packageName;

	private int days;

	private int noofActivities;

	private double pricePerPerson;

	private String packageImgUrl;

	@Column(length = 2000)
	private String description;

	@ManyToMany
	private List<Hotel> hotel;

	@ManyToMany
	private List<Flight> flight;

	@ManyToMany
	private List<Train> train;

	@ManyToMany
	private List<Bus> bus;

	@OneToMany(mappedBy = "package1", cascade = CascadeType.ALL)
	private List<HolidayPackageBook> holidayPackageBooks;

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getNoofActivities() {
		return noofActivities;
	}

	public void setNoofActivities(int noofActivities) {
		this.noofActivities = noofActivities;
	}

	public double getPricePerPerson() {
		return pricePerPerson;
	}

	public void setPricePerPerson(double pricePerPerson) {
		this.pricePerPerson = pricePerPerson;
	}

	public String getPackageImgUrl() {
		return packageImgUrl;
	}

	public void setPackageImgUrl(String packageImgUrl) {
		this.packageImgUrl = packageImgUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Hotel> getHotel() {
		return hotel;
	}

	public void setHotel(List<Hotel> hotel) {
		this.hotel = hotel;
	}

	public List<Flight> getFlight() {
		return flight;
	}

	public void setFlight(List<Flight> flight) {
		this.flight = flight;
	}

	public List<Train> getTrain() {
		return train;
	}

	public void setTrain(List<Train> train) {
		this.train = train;
	}

	public List<Bus> getBus() {
		return bus;
	}

	public void setBus(List<Bus> bus) {
		this.bus = bus;
	}

	public List<HolidayPackageBook> getHolidayPackageBooks() {
		return holidayPackageBooks;
	}

	public void setHolidayPackageBooks(List<HolidayPackageBook> holidayPackageBooks) {
		this.holidayPackageBooks = holidayPackageBooks;
	}

}
