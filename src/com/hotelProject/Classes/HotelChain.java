package com.hotelProject.Classes;
import java.util.List;

public class HotelChain {
	
	private String chainName;
	private int numOfHotels;
	private List<String> emails;
	private List<String> phones;
	private List<Address> addresses; 
	
	public HotelChain(String chainName, int numOfHotels, List<String> emails, List<String> phones,
			List<Address> addresses) {
		this.chainName = chainName; 
		this.numOfHotels = numOfHotels;
		this.emails = emails;
		this.phones = phones; 
		this.addresses = addresses;
	}
	
	public HotelChain(String chainName, int numOfHotels) {
		this.chainName = chainName; 
		this.numOfHotels = numOfHotels;
	}
	
	public HotelChain(String chainName) {
		this.chainName = chainName; 
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public int getNumOfHotels() {
		return numOfHotels;
	}

	public void setNumOfHotels(int numOfHotels) {
		this.numOfHotels = numOfHotels;
	}


	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	@Override
	public String toString() {
		return "HotelChain [chainName=" + chainName + ", numOfHotels=" + numOfHotels + "]";
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

}
