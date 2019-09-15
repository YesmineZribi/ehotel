package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Employee;
import com.hotelProject.Classes.Rent;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class EmployeeProfileDisplayServlet
 */
@WebServlet("/EmployeeProfileDisplayServlet")
public class EmployeeProfileDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeProfileDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		String message = (String)request.getParameter("message");
		String failedMessage = (String)request.getParameter("failedMessage");
		String ssn = (String)request.getParameter("ssn");
		System.out.println("this is the ssn: "+ssn);
		String password = (String)request.getParameter("password");
		System.out.println("this is the password: "+password);
		String userType = (String)request.getParameter("user");
		System.out.println("this is the user type: "+userType);	
		
		//Get employee object
		Employee employee = ac.getEmployee(ssn);
		
		//Get employee address 
		Address address = employee.getAddress();
		
		//Get bookings for hotel employee works ag 
		List<Booking> bookings = ac.getBookingsForHotel(employee.getHotelID());
		
		//Get rents employee is taking care of 
		List<Rent> employeeRents = ac.getRentsForHotel(ssn);
		
		//Get rents to archive
		List<Rent> pastRents = ac.getPastRents(employee.getHotelID());
		
		//Get archived bookings for this hotel 
		List<Booking> archivedBookings = ac.getArchivedBookings(employee.getHotelID());
		
		//Get archived rents for this hotel  
		List<Rent> archivedRents = ac.getArchivedRents(employee.getHotelID());
		
		request.setAttribute("employee", employee);
		request.setAttribute("address", address);
		request.setAttribute("bookings", bookings);
		request.setAttribute("rents", employeeRents);
		request.setAttribute("past_rents", pastRents);
		request.setAttribute("archived_bookings", archivedBookings);
		request.setAttribute("archived_rents", archivedRents);
		request.setAttribute("success_message", message);
		request.setAttribute("failed_message", failedMessage);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("employee_profile.jsp");
		
		dispatcher.forward(request,response);
		
		try {
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
