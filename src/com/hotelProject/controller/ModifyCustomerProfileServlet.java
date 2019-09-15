package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.*;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class modifyCustomerProfileServlet
 */
@WebServlet("/ModifyCustomerProfileServlet")
public class ModifyCustomerProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyCustomerProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		// Step 1: Get all the fields 
		String ssn = request.getParameter("ssn");
		String firstName = request.getParameter("firstName");
		String middleName = request.getParameter("middleName");
		String lastName = request.getParameter("lastName");
		String password = request.getParameter("password");
		String streetNum = request.getParameter("streetNumber");
		String streetName = request.getParameter("streetName");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip-code");
		
		// Step 2: Get the customer object 
		Customer c = ac.getCustomer(ssn);
		
		//Modify all necessary fields in customer 
		c.setFirstName(firstName); c.setMiddleName(middleName); c.setLastName(lastName);
		c.setPassword(password);
		
		// Step 3: Create address object from the address fields 
		Address address;
		String message = ""; String failedMessage = "";
		try {
			 address = new Address(Integer.parseInt(streetNum), streetName, province, country,zipCode);
			
		} catch(NumberFormatException e) {
			//if invalid street num notify user
			failedMessage = "Invalid street number please enter an integer";
			
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);
			return;
		}
		
		c.setAddress(address); //modify address
		
		//Modify all attributes in the database
		ac.modifyFirstName(ssn, firstName);
		ac.modifyMiddleName(ssn, middleName);
		ac.modifyLastName(ssn, lastName);
		ac.modifyPassword(ssn, password);
		ac.modifyAddress(ssn, address);
	
		String successfull = "fields updated successfully!";
		request.setAttribute("success_message", successfull);
		
		message = "fields updated successfully!";
		
		response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);
		
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
