package com.hotelProject.Classes;
import java.sql.Date;

public class Rent {
	private String customerSSN; 
	private String rentID;
	private String employeeSSN;
	private String hotelID;
	private int roomNumber; 
	private Date startDate; 
	private Date endDate; 
	private double rentRate;
	private double customerBalance; 
	
	public Rent(String customerSSN, String rentID, String employeeSSN, String hotelID, int roomNumber, Date startDate,
			Date endDate, double rentRate, double customerBalance) {
		this.customerSSN = customerSSN; 
		this.rentID = rentID;
		this.employeeSSN = employeeSSN;
		this.hotelID = hotelID; 
		this.roomNumber = roomNumber; 
		this.startDate = startDate; 
		this.endDate = endDate; 
		this.rentRate = rentRate;
		this.customerBalance = customerBalance;
	}

	public String getEmployeeSSN() {
		return employeeSSN;
	}

	public void setEmployeeSSN(String employeeSSN) {
		this.employeeSSN = employeeSSN;
	}

	public String getCustomerSSN() {
		return customerSSN;
	}

	public void setCustomerSSN(String customerSSN) {
		this.customerSSN = customerSSN;
	}

	public String getRentID() {
		return rentID;
	}

	public void setRentID(String rentID) {
		this.rentID = rentID;
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

	public double getRentRate() {
		return rentRate;
	}

	public void setRentRate(double rentRate) {
		this.rentRate = rentRate;
	}

	public double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public String toString() {
		return "Customer: "+customerSSN+" Hotel: "+hotelID+" room: "+roomNumber;
	}
}
