package com.hotelProject.Classes;
import java.util.Date;

public class Customer extends Person{

	private Date registrationDate; 
	
	public Customer(String ssn, String firstName, String middleName, String lastName, Address address,
			Date registrationDate, String password) {
		super(ssn, firstName, middleName, lastName, address, password);
		this.registrationDate = registrationDate;

	}
	

	public Customer() {
		
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


	@Override
	public String toString() {
		return "Customer [getSsn()=" + getSsn() + ", getFirstName()=" + getFirstName() + ", getMiddleName()="
				+ getMiddleName() + ", getLastName()=" + getLastName() + "]";
	}
}
