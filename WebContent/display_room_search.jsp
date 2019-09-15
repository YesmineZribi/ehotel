<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | SearchRooms</title>
</head>
<body>

	<h3 align=center>Search Results</h3>
	<c:choose>
	<c:when test="${fn:length(search_result) eq 0}">
		
	<p>No Rooms Found...</p>

	</c:when>
	
	
	
	
	<c:otherwise>
	<form action="DisplayRoomSearchServlet" method="GET">
		<table border=1>
			<tr>
				<th>Hotel ID</th>
				<th>Room Number</th>
				<th>Capacity</th>
				<th>View Type</th>
				<th>Can be Extended</th>
				<th>Problem</th>
				<th>Price (Amenity included)</th>
				<th></th>
			</tr>
			<tr>
				<c:forEach var="room" items="${search_result}">
					<tr>
					
						<td>${room.hotelID}</td>
						<td>${room.roomNumber}</td>
						<td>${room.capacity}</td>
						<td>${room.viewType}</td>
						<td>${room.isExtended}</td>
						<td>${room.problem}</td>
						<td>$${room.price}</td>
						<td>
						<input type="hidden" name="customer_ssn" value="${customer_ssn}">
						<input type="submit" name="${room.hotelID}-${room.roomNumber}-view" value="See More">
						</td>
				
					</tr>
				</c:forEach>	
			</tr>	
		</table>
	</form>
	</c:otherwise>
	</c:choose>

</body>
</html>