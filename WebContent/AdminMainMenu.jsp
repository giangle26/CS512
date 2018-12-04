<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Main Menu</title>
</head>
<body>
<a href="home.html">Home</a>
	<%@ page import="java.sql.*"%>
	<%@ page import="javax.sql.*"%>
	<%@ page import ="java.util.ArrayList"%>
	<%@ page import ="java.util.List"%>
	<%@ page import ="java.util.Set"%>
	<%@ page import ="java.util.LinkedHashSet"%>
	
	<%
	Class.forName("com.mysql.jdbc.Driver"); 
	java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://cs512db.csab6py4gsye.us-east-1.rds.amazonaws.com:3306/CS512",
	"admincs512","Matkhau18!");
	%>
	
	<h2>List of Users</h2>
	<br/><br/>
	<table border="1">
	<tr>
		<td>Type</td>
		<td>Object to deliver</td>
		<td>From Location</td>
		<td>From Time</td>
		<td>To Location</td>
		<td>To Time</td>
	</tr>
	<%
	Set<ArrayList<String>> totalSet = new LinkedHashSet<ArrayList<String>>();
	Statement st= con.createStatement(); 
	ResultSet rs=st.executeQuery("select * from Requests");
	while(rs.next()){
		%>
		<tr>
	    <td><%out.print(rs.getString(1)); %></td>
	    <td><%out.print(rs.getString(2)); %></td>
	    <td><%out.print(rs.getString(3)); %></td>
	    <td><%out.print(rs.getString(4)); %></td>
	    <td><%out.print(rs.getString(5)); %></td>
	    <td><%out.print(rs.getString(6)); %></td>
		 </tr>
		<%    
	}	
	
	%>
	</table>
	
</body>
</html>