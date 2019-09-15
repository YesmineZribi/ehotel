package com.hotelProject.Classes;
import java.util.Date;
import java.util.List;

public class Hotel {
	private String hotelChainName;
	private String hotelID;
	private String hotelName;
	private Address address; 
	private List<String> phones; 
	private List<String> emails;
	private String managerSSN;
	private Date startDate; 
	private int numOfRooms;
	private int category;
	
	public Hotel(String hotelChainName, String hotelID, String hotelName, Address address, List<String> phones,
			List<String> emails, String managerSSN, Date startDate, int numOfRooms, int category) {
		this.hotelChainName = hotelChainName;
		this.hotelID = hotelID;
		this.hotelName = hotelName; 
		this.address = address; 
		this.phones = phones; 
		this.emails = emails; 
		this.managerSSN = managerSSN; 
		this.startDate = startDate;
		this.numOfRooms = numOfRooms;
		this.category = category; 
	}
	
	public Hotel(String hotelChainName, String hotelID, String hotelName) {
		this.hotelChainName = hotelChainName;
		this.hotelID = hotelID;
		this.hotelName = hotelName;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Hotel() {
		
	}
	
	public String getHotelChainName() {
		return hotelChainName;
	}

	public void setHotelChainName(String hotelChainName) {
		this.hotelChainName = hotelChainName;
	}

	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getManagerSSN() {
		return managerSSN;
	}

	public void setManagerSSN(String managerSSN) {
		this.managerSSN = managerSSN;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getNumOfRooms() {
		return numOfRooms;
	}

	public void setNumOfRooms(int numOfRooms) {
		this.numOfRooms = numOfRooms;
	}

	public String toString() {
		return "hotel: "+hotelName+", hotelChainName: "+hotelChainName+" phones: "+phones+" emails :"+emails; 
	}
	
}
