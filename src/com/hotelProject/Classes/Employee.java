package com.hotelProject.Classes;
import java.util.List;

public class Employee extends Person {
	
	private List<String> roles; 
	private String hotelID; 
	
	public Employee(String ssn, String firstName, String middleName, String lastName, Address address,
			List<String> roles, String hotelID, String password) {
		super(ssn, firstName, middleName, lastName, address, password);
		this.roles = roles; 
		this.hotelID = hotelID; 
	}

	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(String hotelID) {
		this.hotelID = hotelID;
	}

	public Employee() {
		
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String role) {
		roles.add(role);
	}
	
	public void deleteRole(String role) {
		roles.remove(role);
	}

	@Override
	public String toString() {
		return "Employee [hotelID=" + hotelID + ", getFirstName()=" + getFirstName() + ", getMiddleName()="
				+ getMiddleName() + ", getLastName()=" + getLastName() + "]";
	}
	
}
