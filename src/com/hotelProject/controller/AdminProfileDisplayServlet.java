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

import com.hotelProject.Classes.HotelChain;
import com.hotelProject.Classes.NumOfRoomsPerArea;
import com.hotelProject.Classes.RoomCapacity;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class AdminProfileDisplayServlet
 */
@WebServlet("/AdminProfileDisplayServlet")
public class AdminProfileDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminProfileDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the admin information 
		String admin = request.getParameter("ssn");
		String password = request.getParameter("password");
		
		//Get all the hotel chains 
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		List<HotelChain> hotelChains = hr.getAllHotelChains();
		
		//Get both views 
		List<NumOfRoomsPerArea> view1 = hr.getView1();
		List<RoomCapacity> view2 = hr.getView2();
		
		request.setAttribute("hotel_chains", hotelChains);
		request.setAttribute("admin",admin);
		request.setAttribute("password", password);
		request.setAttribute("view1", view1);
		request.setAttribute("view2", view2);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("admin_profile.jsp");
		dispatcher.forward(request, response);
		
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
