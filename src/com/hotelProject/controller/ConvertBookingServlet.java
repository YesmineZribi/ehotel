package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Employee;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.BRManagement;

/**
 * Servlet implementation class ConvertBookingServlet
 */
@WebServlet("/ConvertBookingServlet")
public class ConvertBookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConvertBookingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		BRManagement br = new BRManagement();
		
		String employeeSSN = request.getParameter("employee_ssn");
		String hotelID = request.getParameter("hotel_id");
		
		//Get employee object 
		Employee employee = ac.getEmployee(employeeSSN);
		
		//Get all bookings for hotel 
		List<Booking> bookings = ac.getBookingsForHotel(hotelID);
		Booking bookingSelected = null; 
		String tempButton = "";
		//Figure out which one's bookingID matches the button pressed
		for (Booking b: bookings) {
			tempButton = request.getParameter(b.getBookingID()+"-Convert");
			if (tempButton != null) { //found the button pressed
				bookingSelected = b;
				
			}
		}
		
		//Convert that one 
		boolean valid = br.convertBooking(employeeSSN, bookingSelected.getCustomerSSN(), bookingSelected.getBookingID());
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (valid) {
			 message = "Booking successfully converted!";
			
		} else {
			failedMessage = "Could not convert booking";
		}
		
		response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);


		try {
			br.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
