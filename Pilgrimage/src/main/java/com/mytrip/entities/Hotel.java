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
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hId;

	private String hotelName;

	private String location;

	private int noofPerson;

	private double price;

	private String type;

	private int noofRooms;

	private String description;

	private String hotelImgUrl;

	@ManyToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
	private List<Package> package1;

	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
	private List<HotelBook> hotelBooks;

	public Hotel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int gethId() {
		return hId;
	}

	public void sethId(int hId) {
		this.hId = hId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getNoofPerson() {
		return noofPerson;
	}

	public void setNoofPerson(int noofPerson) {
		this.noofPerson = noofPerson;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNoofRooms() {
		return noofRooms;
	}

	public void setNoofRooms(int noofRooms) {
		this.noofRooms = noofRooms;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHotelImgUrl() {
		return hotelImgUrl;
	}

	public void setHotelImgUrl(String hotelImgUrl) {
		this.hotelImgUrl = hotelImgUrl;
	}

	public List<Package> getPackage1() {
		return package1;
	}

	public void setPackage1(List<Package> package1) {
		this.package1 = package1;
	}

	public List<HotelBook> getHotelBooks() {
		return hotelBooks;
	}

	public void setHotelBooks(List<HotelBook> hotelBooks) {
		this.hotelBooks = hotelBooks;
	}

}
