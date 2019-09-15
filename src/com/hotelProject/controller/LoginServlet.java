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
import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Employee;
import com.hotelProject.Classes.Rent;
import com.hotelProject.model.AccountManagement;;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		// Step 1: verify user exists 
		String ssn = request.getParameter("ssn");
		String password = request.getParameter("password");
		String userType = request.getParameter("user");
		
		boolean valid = false;
		if (userType.equals("customer")) {
			valid = ac.verifyCustomer(ssn, password);
		} else if (userType.contentEquals("employee")){
			valid = ac.verifyEmployee(ssn, password);
		} else { //if it is the admin logging in 
			if (ssn.contentEquals("admin") && password.contentEquals("passadmin")) {
				valid = true;
			}
		}
		
		// Step 2: if not valid display error message
		RequestDispatcher dispatcher;
		if (!valid) {

			String errorMessage = "Wrong password or user does not exist - try again";
			request.setAttribute("error_message", errorMessage);
			
			dispatcher = request.getRequestDispatcher("login_signup.jsp");
			
			dispatcher.forward(request, response);
			
		} else { //Step 2: if valid go to profile page
			if (userType.equals("customer")) { //if user is customer
				
				//Get customer object 
				Customer customer = ac.getCustomer(ssn);
				
				//Get bookings for customer 
				List<Booking> bookings = ac.getBookingsForCustomer(ssn);			
				
				//Get rents for customer 
				List<Rent> rents = ac.getRentsForCustomer(ssn);
				
				
				request.setAttribute("customer", customer);
				request.setAttribute("address",customer.getAddress());
				request.setAttribute("bookings", bookings);
				request.setAttribute("rents", rents);
				
				dispatcher = request.getRequestDispatcher("customer_profile.jsp");
				
				dispatcher.forward(request, response);
				
			} else if (userType.contentEquals("employee")) { //if user is employee
				//Get employee object
				Employee employee = ac.getEmployee(ssn);
				
				System.out.println("THIS IS THE EMPLOYEE: "+employee.getFirstName());
				
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
				
				dispatcher = request.getRequestDispatcher("employee_profile.jsp");
				
				dispatcher.forward(request,response);
				
				
			} else { //if user is admin 
				
				response.sendRedirect("AdminProfileDisplayServlet?ssn=admin&password=passadmin");
				
			}
			
			
		}
		
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
