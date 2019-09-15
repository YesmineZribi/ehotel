package com.hotelProject.Classes;

public class Address {
	
	private int streetNum;
	private String streetName; 
	private String province; 
	private String country;
	private String zipCode; 
	
	public Address(int streetNum, String streetName, String province, String country, String zipCode) {
		this.streetNum = streetNum;
		this.streetName = streetName; 
		this.province = province; 
		this.country = country; 
		this.zipCode = zipCode; 
	}
	
	public Address() {
		
	}

	public int getStreetNum() {
		return streetNum;
	}

	public void setStreetNum(int streetNum) {
		this.streetNum = streetNum;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	

	
}
