<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Helper Availability</title>
</head>
<body>
<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<%
	String locFrom=request.getParameter("locFrom"); 
	String beginTime=request.getParameter("beginTime"); 
	String locTo=request.getParameter("locTo"); 
	String endTime=request.getParameter("endTime"); 
	
	if (locFrom.isEmpty()) {
		out.println("From Location field is empty.");
	} else if (beginTime.isEmpty()) {
		out.println("Available Time field is empty.");
	} else if (locTo.isEmpty()) {
		out.println("To Location field is empty.");
	} else if (endTime.isEmpty()) {
		out.println("Arrive by Time field is empty.");
	} else {
		Class.forName("com.mysql.jdbc.Driver"); 
		java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://cs512db.csab6py4gsye.us-east-1.rds.amazonaws.com:3306/CS512",
		"admincs512","Matkhau18!"); 
		Statement st= con.createStatement(); 
		int i=st.executeUpdate("insert into Requests values ('helper',null,'"+locFrom+"','"+beginTime+"','"+locTo+"','"+endTime+"')"); 
		out.println("Submitted, thanks for helping"); 
	}
%>
<a href="home.html">Home</a>
</body>
</html>