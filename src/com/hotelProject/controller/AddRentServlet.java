package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Rent;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.BRManagement;

/**
 * Servlet implementation class AddRentServlet
 */
@WebServlet("/AddRentServlet")
public class AddRentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BRManagement br = new BRManagement();
		AccountManagement ac = new AccountManagement();
		
		String customerSSN = request.getParameter("customer_ssn");
		String employeeSSN = request.getParameter("employee_ssn");
		String hotelID = request.getParameter("hotel_id");
		int roomNumber = Integer.parseInt(request.getParameter("room_number"));
		
		
		System.out.println("hotelID is "+hotelID);
		//Get all rents 
		List<Rent> rents = ac.getAllRents();
		List<Integer> rentIDs = new ArrayList<Integer>();
		int rentID = 0;
		if (rents.size() != 0) {
			//Get rent ID; 
			for (Rent re: rents) {
				rentIDs.add(Integer.parseInt(re.getRentID()));
			}
			
			//Get biggest rentID 
			int maxRentID = rentIDs.get(0);
			for (int i = 0; i < rentIDs.size(); i++) {
				if (rentIDs.get(i) > maxRentID) {
					maxRentID = rentIDs.get(i);
				}
			}
			rentID = maxRentID+1;				
		}
		
		
		String employeePassword = request.getParameter("employee_password");

		double rent = Double.parseDouble(request.getParameter("rent"));
		String customerB = request.getParameter("customer_balance") ;
		double customerBalance;
		if (customerB.equals("")) {
			customerBalance = 0.0;
		} else {
			customerBalance = Double.parseDouble(request.getParameter("customer_balance"));	
		}
		
		java.util.Date startDate, endDate;
		startDate = endDate = null;
		try {
			 startDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("start_date"));
			 endDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("end_date"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			String message = "";
			String failedMessage = "Illegal date format";
			response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employeeSSN+"&password="+employeePassword+"&user=employee&message="+message+"&failedMessage="+failedMessage);
			return;
		}
		
		Rent r = new Rent(customerSSN,String.valueOf(rentID),employeeSSN,hotelID, roomNumber, new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()), rent, customerBalance);
		
		//add rent to db
		boolean valid = br.addRent(r);
		String message, failedMessage;
		message = failedMessage = "";
		if (valid) {
			message = "Rent added successfully!";		
		} else {
			failedMessage = "Failed to add rent";
		}
		

		response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employeeSSN+"&password="+employeePassword+"&user=employee&message="+message+"&failedMessage="+failedMessage);
		
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
