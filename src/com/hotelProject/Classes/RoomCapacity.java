package com.hotelProject.Classes;

public class RoomCapacity {
	
	private String hotelID;
	private int roomNum; 
	private int capacity;
	public RoomCapacity(String hotelID, int roomNum, int capacity) {
		super();
		this.hotelID = hotelID;
		this.roomNum = roomNum;
		this.capacity = capacity;
	}
	public String getHotelID() {
		return hotelID;
	}
	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	} 
	
	

}
