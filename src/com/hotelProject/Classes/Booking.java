package com.hotelProject.Classes;
import java.sql.Date;

public class Booking {
	private String customerSSN; 
	private String bookingID;
	private String hotelID;
	private int roomNumber; 
	private Date startDate; 
	private Date endDate; 
	private double pricePerNight;
	
	public Booking(String customerSSN, String bookingID, String hotelID, int roomNumber, Date startDate,
			Date endDate, double pricePerNight) {
		this.customerSSN = customerSSN; 
		this.bookingID = bookingID;
		this.hotelID = hotelID; 
		this.roomNumber = roomNumber; 
		this.startDate = startDate; 
		this.endDate = endDate; 
		this.pricePerNight = pricePerNight;
	}

	public String getCustomerSSN() {
		return customerSSN;
	}

	public void setCustomerSSN(String customerSSN) {
		this.customerSSN = customerSSN;
	}

	public String getBookingID() {
		return bookingID;
	}

	public void setBookingID(String boookingID) {
		this.bookingID = boookingID;
	}

	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}
	
	public String toString() {
		return "Customer: "+customerSSN+" Hotel: "+hotelID+" room: "+roomNumber;
	}

}
