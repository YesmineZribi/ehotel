package com.hotelProject.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.CentralOffice;
import com.hotelProject.Classes.Hotel;
import com.hotelProject.Classes.HotelChain;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class DisplayHotelChainInfoServlet
 */
@WebServlet("/DisplayHotelChainInfoServlet")
public class DisplayHotelChainInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayHotelChainInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the admin stuff 
		String admin = request.getParameter("admin");
		String password = request.getParameter("password");
		
		//Figure out which button was hit
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//1. Get all the hotel chains
		List<HotelChain> hotelChains = hr.getAllHotelChains();
		HotelChain chainSelected = null; 
		String tempButton;
		for(HotelChain chain : hotelChains) {
			tempButton = request.getParameter(chain.getChainName()+"-see-more");
			if (tempButton != null) {
				chainSelected = chain; 
			}
		}
		
		//if we do not come from main page
		if (chainSelected == null) {
			//Get the chain based on the chainName
			String hotelChain = URLDecoder.decode(request.getParameter("hotel_chain"), "UTF-8");
			String message = request.getParameter("message");
			String failedMessage = request.getParameter("failed_message");
			
			
			
			System.out.println("Chain name received: "+hotelChain);
			//Set chainSelected to be that chain
			chainSelected = hr.getHotelChain(hotelChain);
			
			request.setAttribute("message", message);
			request.setAttribute("failed_message", failedMessage);
			
		}
		
		//Get emails of hotel chain selected
		List<String> chainEmails = hr.chainEmails(chainSelected.getChainName());
		chainSelected.setEmails(chainEmails);
		
		//Get phones of hotel chain selected
		List<String> chainPhones = hr.chainPhones(chainSelected.getChainName());
		chainSelected.setPhones(chainPhones);
		
		//Get central offices of chain
		List<CentralOffice> centralOffices = hr.getOffices(chainSelected.getChainName());
		
		//Get the hotels of that chain 
		List<Hotel> hotels = hr.getAllChainHotels(chainSelected.getChainName());
		
		request.setAttribute("hotel_chain", chainSelected);
		request.setAttribute("hotels", hotels);
		request.setAttribute("central_offices", centralOffices);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("chain_hotels_info.jsp");
		
		dispatcher.forward(request, response);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}
