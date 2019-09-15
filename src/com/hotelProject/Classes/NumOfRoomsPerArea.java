package com.hotelProject.Classes;

public class NumOfRoomsPerArea {
	
	private String province; 
	private int numOfRooms;
	public NumOfRoomsPerArea(String province, int numOfRooms) {
		super();
		this.province = province;
		this.numOfRooms = numOfRooms;
	}
	
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public int getNumOfRooms() {
		return numOfRooms;
	}
	public void setNumOfRooms(int numOfRooms) {
		this.numOfRooms = numOfRooms;
	} 

}
