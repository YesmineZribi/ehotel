<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import ="com.hotelProject.Classes.DateInterval" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Room Selected</title>
</head>
<body>
		<h3 align=center>Selected Room Information</h3>
		
	<p style="color:red">${failed_message}</p>		
	<p style="color:green">${message}</p>

<div class="room-info">
	<form action="BookRoomServlet">
		Hotel Name: <input type="text" name="hotel_name" value="${hotel_name}" readonly><br><br>
		Hotel ID: <input type="text" name="hotel_id" value="${room.hotelID}" readonly><br><br>
		Hotel Chain: <input type="text" name="chain_name" value="${chain_name}" readonly><br><br>
		Room Number: <input type="text" name="room_num" value="${room.roomNumber}" readonly><br><br>
		Capacity: <input type="text" name="capacity" value="${room.capacity}" readonnly><br><br>
		View Type: 
		<c:if test="${room.viewType eq 'SeaView'}">
			<input type="hidden" name="view_type_string" value="${room.viewType}">
			<input type="text" name="view_type" value="Sea View" readonly><br><br>
		</c:if>
		<c:if test="${room.viewType eq 'MountainView'}">
			<input type="hidden" name="view_type_string" value="${room.viewType}">
			<input type="text" name="view_type" value="Mountain View" readonly><br><br>
		</c:if>
		Is Extended: <input type="text" name="is_extended" value="${room.isExtended}" readonly> <br><br>
		Problem: <input type="text" name="problem" value="${room.problem}" readonly> <br><br>
		Price: $<input type ="text" name="price" value="${room.price}" readonly> 
		<i>*amenity price included</i> <br><br>
		
		Please enter dates not already booked/rented: <br><br>		
		<strong>Start Date</strong>: <input  type="date" name="start_date" required placeholder="YYYY-MM-DD"> &nbsp &nbsp
		<strong>End Date</strong>: <input type="date" name="end_date" required placeholder="YYYY-MM-DD"><br>
		
		<br><br>
		<c:if test="${fn:length(unavailable_dates) gt 0}">
		<h5>Dates already booked</h5>
		<c:forEach var="unavail_date" items="${unavailable_dates}">
			<p style="font-size:13px">* ${unavail_date}</p> 
		</c:forEach>
		</c:if>
		
		<c:if test="${fn:length(amenities) gt 0 }">
			<h4 align=center> Amenity Information</h4>
			<div align=center>
			<table border=1>
				<tr>
					<th>Type</th>
					<th>Price</th>
				</tr>
				<c:forEach var="amenity" items="${amenities}">
					<tr>
						<td>${amenity.type}</td>
						<td>${amenity.price}</td>
					
					</tr>
				</c:forEach>
			</table>
			</div>
		</c:if>
		<input type="hidden" name="customer_ssn" value="${customer_ssn}">
		<div style="display:inline-block; float:right" align=right>
		<input type="submit" value="Book Room">
		</div>
		
	</form>
	
<div class="go-back" style="display:inline-block; float:left">
<form action="GoBackServlet">
	<input type="hidden" name="customer_ssn" value="${customer_ssn}">
	<input type="submit" name="selected-room-info" value="Click to go back to main page">
</form>

</div>

</div>






</body>
</html>