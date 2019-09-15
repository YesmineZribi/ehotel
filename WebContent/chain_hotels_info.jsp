<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hotel Project | Chain And Hotels</title>
</head>
<body>

	<h3 align=center>${hotel_chain.chainName}'s information</h3>
	
	<p style="color:green">${message}</p>
	<p style="color:red">${failed_message}</p>

	<div align=center name="chain-emails">
		<h4>Chain Emails</h4>
		<table border=1>
			<tr>
				<th>Email</th>
			</tr>
			<c:forEach var="email" items="${hotel_chain.emails}">
				<tr>
					<td>${email}</td>
				</tr>
			</c:forEach>
		
		</table>
	</div>
	<br>
	<hr>
	<div align=center name="chain-phones">
		<h4>Chain Phones</h4>
		<table border=1>
			<tr>
				<th>Phone</th>
			</tr>
			<c:forEach var="phone" items="${hotel_chain.phones}">
				<tr>
					<td>${phone}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<hr>
	<br>
	
	<div align=center>
		<h4>Central Offices</h4>
		<table border=1>
			<tr>
				<th>Address</th>
			</tr>
			
			<c:forEach var="office" items="${central_offices}">
				<tr>
					<td>${office.address.streetNum} ${office.address.streetName},
						${office.address.province}, ${office.address.country},
						${office.address.zipCode}</td>
				</tr>			
			</c:forEach>			
		</table>
	</div>
	<br>
	<hr>
	
	<div align=center name="delete-hotels">
		<h4>Hotels Owned By ${hotel_chain.chainName}</h4>
		<form action="DeleteOrAddHotelsServlet" method="GET">
			<table border=1>
				<tr>
					<th>Hotel ID</th>
					<th>Hotel Name</th>
					<th>Category</th>
					<th>Manager SSN</th>
					<th>Manager Start Date</th>
					<th>Number of Rooms</th>
					<th>Address</th>
					<th></th>
				</tr>
				<c:forEach var="hotel" items="${hotels}">
					<tr>
						<td>${hotel.hotelID}</td>
						<td>${hotel.hotelName}</td>
						<td>${hotel.category}</td>
						<td>${hotel.managerSSN}</td>
						<td>${hotel.startDate}</td>
						<td>${hotel.numOfRooms}</td>
						<td>${hotel.address.streetNum}
							${hotel.address.streetName},
							${hotel.address.province},
							${hotel.address.country},
							${hotel.address.zipCode}
						</td>
						<td>
							<input type="hidden" name="hotel_chain" value="${hotel_chain.chainName}">
							<input type="submit" name="${hotel.hotelID}_delete" value="Delete">
						</td>	
					</tr>
				</c:forEach>	
			</table>
			<br><br>
		</form>
	</div>
	<div name="add-hotels">
		<h4 align=center>Add A Hotel</h4>
		<form action="DeleteOrAddHotelsServlet" method="GET">
			Hotel ID: <input type="text" name="hotel_id" required><br><br>
			Hotel Name: <input type="text" name="hotel_name" required><br><br>
			Category: <input type="number" name="category" min="1" max="5" oninput="this.value=(parseInt(this.value)||'')" required><br><br>
			Address: <br><br> 
			Street Number: <input type="number" oninput="this.value=(parseInt(this.value)||'')" min= 1 name="street_num" required><br><br>
			Street Name: <input type="text" name="street_name" required><br><br>
			Province: <input type="text" name="province" required><br><br>
			Country: <input type="text" name="country" required><br><br>
			Zip Code: <input type="text" name="zip_code" required><br><br>
			<input type="hidden" name="hotel_chain" value="${hotel_chain.chainName}"><br><br>
			
			<div style="display:inline-block; float:right">
				<input type="submit" name="add-hotel" value="Add Hotel">
			</div>
		</form>
	
	
	</div>
	
	<div class="go-back" style="display:inline-block; float:left">
		<form action="GoBackServlet">
			<input type="submit" name="chain-info" value="Click to go back to main page">
		</form>
	</div>

</body>
</html>