package com.mytrip.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HolidayPackageBook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int hpackageBookId;

	private int noOfPerson;

	private String date;

	private String address;

	private double bookingPrice;

	private String status;

	private String orderId;

	@ManyToOne
	private User user;

	@ManyToOne
	private Package package1;

	public int getHpackageBookId() {
		return hpackageBookId;
	}

	public void setHpackageBookId(int hpackageBookId) {
		this.hpackageBookId = hpackageBookId;
	}

	public int getNoOfPerson() {
		return noOfPerson;
	}

	public void setNoOfPerson(int noOfPerson) {
		this.noOfPerson = noOfPerson;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getBookingPrice() {
		return bookingPrice;
	}

	public void setBookingPrice(double bookingPrice) {
		this.bookingPrice = bookingPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Package getPackage1() {
		return package1;
	}

	public void setPackage1(Package package1) {
		this.package1 = package1;
	}

}
