<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hotel Project | Admin Profile</title>
</head>
<body>

	<h3>Welcome, Admin</h3>
	<form action="GoBackServlet">
		<input type="submit" name="log-out" value="Log Out">
		
	</form>
	<br><br>



	<h3 align=center>Hotel Chains</h3>
	
	<div align=center>
	<form action="DisplayHotelChainInfoServlet" method="GET">
		<table border=1>
			<tr>
				<th>Hotel Chain Name</th>
				<th>Number of Hotels Owned</th>
				<th></th>
			</tr>
			<c:forEach var="hotel_chain" items="${hotel_chains}">
				<tr>
					<td>${hotel_chain.chainName}</td>
					<td>${hotel_chain.numOfHotels}</td>
					<td>
					<input type="hidden" name="admin" value="${admin}">
					<input type="hidden" name="password" value="${password}">
					<input type="submit" name="${hotel_chain.chainName}-see-more" value="See More">
					
					</td>
				
				</tr>
			
			</c:forEach>
			
		</table>
	
	</form>
	
	</div>
	
	<!--  
	<br><hr>
	<div class="views" align=center>
		<h4>View 1: Number of Rooms Per Area</h4>
		
		<table border=1>
			<tr>
				<th>Province</th>
				<th>Number Of Rooms Per Area</th>
			</tr>
			<c:forEach var="view_elem" items="${view1}">
				<tr>
					<td>${view_elem.province}</td>
					<td>${view_elem.numOfRooms}</td>
				</tr>
			</c:forEach>

		</table>
		<br><hr>
		<h4>View 2: Room Capacity of all Hotels</h4>
		
		<table border=1>
			<tr>
				<th>Hotel ID</th>
				<th>Room Number</th>
				<th>Capacity</th>
			</tr>
			<c:forEach var="view_elem" items="${view2}">
				<tr>
					<td>${view_elem.hotelID }</td>
					<td>${view_elem.roomNum }</td>
					<td>${view_elem.capacity}</td>	
				</tr>
			</c:forEach>	
		
		</table>	
	</div>
 	-->
</body>
</html>