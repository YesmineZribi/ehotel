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
 * Servlet implementation class ArchiveRentServlet
 */
@WebServlet("/ArchiveRentServlet")
public class ArchiveRentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArchiveRentServlet() {
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
		
		//Get the list of past rents 
		List<Rent> rents = ac.getPastRents(hotelID);
		Rent rentSelected = null; 
		String tempButton = "";
		//Figure out which one's rentID matches the button pressed
		for (Rent r: rents) {
			tempButton = request.getParameter(r.getRentID()+"-Archive");
			if (tempButton != null) { //found the button pressed
				rentSelected = r;
			}
		}
		
		System.out.println("Customer balance in selected past rent "+rentSelected.getCustomerBalance());
		//Archive that one 
		boolean valid = br.archiveRent(rentSelected.getCustomerSSN(), rentSelected.getRentID());
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (valid) {
			message = "Rent archived successfully!";
		} else {
			failedMessage = "Could archive rent, please try again!";
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
