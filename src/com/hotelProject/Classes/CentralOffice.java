package com.hotelProject.Classes;

public class CentralOffice {
	private Address address;
	private String chainName;
	public CentralOffice(Address address, String chainName) {
		super();
		this.address = address;
		this.chainName = chainName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	

}
