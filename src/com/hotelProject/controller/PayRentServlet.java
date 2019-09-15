package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Employee;
import com.hotelProject.Classes.Rent;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.BRManagement;

/**
 * Servlet implementation class PayRentServlet
 */
@WebServlet("/PayRentServlet")
public class PayRentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PayRentServlet() {
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

		//Get employee object 
		Employee employee = ac.getEmployee(employeeSSN);
		
		//Get all rents for hotel
		List<Rent> rents = ac.getRentsForHotel(employeeSSN);
		
		//Figure out which one's rentID matches the button pressed
		Rent rentSelected = null;
		String tempButton = "";
		for (Rent r: rents) {
			tempButton = request.getParameter(r.getRentID()+"-Pay");
			if (tempButton != null) {
				rentSelected = r; 
			}
		}
		
		//Now that we know the rentID grab the amount necessary 
		String amount = request.getParameter(rentSelected.getRentID()+"-Amount");

		String message, failedMessage; 
		message = failedMessage = "";
		if (amount.contentEquals("")) {
			failedMessage = "Amount field is invalid, please enter a valid amount";
			response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);
			return;
			
		}
		//Pay the rent 
		boolean valid = br.payRent(rentSelected.getCustomerSSN(), rentSelected.getRentID(), Double.parseDouble(amount));

		if (valid) {
			message = "Rent paid successfully!";
		} else {
			failedMessage = "Could not pay rent, please try again";
		}
		
		response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);

		try {
			ac.getConnection().close();
			br.getConnection().close();
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
