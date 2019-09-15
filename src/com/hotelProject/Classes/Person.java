package com.hotelProject.Classes;

public class Person {
	private String ssn;
	private String firstName; 
	private String middleName; 
	private String lastName; 
	private Address address; 
	private String password; 
	
	public Person(String ssn, String firstName, String middleName, String lastName, Address address, String password) {
		this.ssn = ssn;
		this.firstName = firstName; 
		this.middleName = middleName; 
		this.lastName = lastName; 
		this.address = address; 
		this.password = password; 
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person() {
		
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
