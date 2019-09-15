package com.hotelProject.Classes;
public class Room {
	
	private String hotelID;
	private double price; 
	private int capacity;
	private String viewType;
	private boolean isExtended; 
	private String problem;
	private int roomNumber;
	
	public Room(String hotelID, int roomNumber) {
		this.hotelID = hotelID;
		this.roomNumber = roomNumber; 
	}
	
	public Room(String hotelID, double price, int capacity, String viewType, boolean isExtended, String problem,
			int roomNumber) {
		this.hotelID = hotelID;
		this.price = price; 
		this.viewType = viewType;
		this.isExtended = isExtended; 
		this.problem = problem;
		this.roomNumber = roomNumber; 
		this.capacity = capacity;
	}

	public String getHotelID() {
		return hotelID;
	}

	public boolean getIsExtended() {
		return isExtended;
	}

	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}


	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public String toString() {
		return "Hotel: "+hotelID+"- room: "+roomNumber;
	}
	
}
