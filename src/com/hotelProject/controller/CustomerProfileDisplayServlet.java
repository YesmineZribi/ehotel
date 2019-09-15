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

import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Rent;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class CustomerProfileDisplayServlet
 */
@WebServlet("/CustomerProfileDisplayServlet")
public class CustomerProfileDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerProfileDisplayServlet() {
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
		request.setAttribute("success_message", message);
		request.setAttribute("failed_message", failedMessage);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer_profile.jsp");
		
		dispatcher.forward(request, response);		
		
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
