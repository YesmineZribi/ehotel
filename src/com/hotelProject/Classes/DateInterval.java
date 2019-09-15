package com.hotelProject.Classes;
import java.util.Date;

public class DateInterval {
	
	private Date startDate; 
	private Date endDate; 
	
	public DateInterval(Date startDate, Date endDate) {
		this.startDate  = startDate; 
		this.endDate = endDate; 
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	public boolean isBetween(Date givenDate) {
		if (givenDate.equals(startDate)) {
			return true;
		}
		else if (givenDate.after(startDate) && givenDate.before(endDate)) {
			return true;
		} else {
			return false; 
		}
	}
	
	public String toString() {
		return new java.sql.Date(startDate.getTime())+" to "+new java.sql.Date(endDate.getTime());
	}

	
}