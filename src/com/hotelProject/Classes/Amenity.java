package com.hotelProject.Classes;

public class Amenity {

	private String type; 
	private String hotelID; 
	private int roomNumber; 
	private double price; 
	
	public Amenity(String type, String hotelID, int roomNumber, double price) {
		this.type = type; 
		this.hotelID = hotelID;
		this.roomNumber = roomNumber; 
		this.price = price; 
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString() {
		return type+" - "+price; 
	}
	
	
}
