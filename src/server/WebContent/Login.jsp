<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>
Tech-freaks.in - SimpleLogin App Login Page
</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<form name="frmLogin" method="POST" action="LogonServlet">
<table border="1">
	<tbody>
		<tr>
			<td colspan="2"><c:out value="${errorMsg}"/> </td>
		</tr>
		<tr>
			<td>User Name: </td>
			<td><input type="text" name="email" /></td>
		</tr>
		<tr>
			<td>Password: </td>
			<td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" name="Submit" value="Submit"/></td>
		</tr>
	</tbody>
</table>
</form>
</body>
</html>