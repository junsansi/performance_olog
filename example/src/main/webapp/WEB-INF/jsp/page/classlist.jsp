<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>班级列表</title>
</head>
<body>
<h1><a href="/index.html">索引</a></h1>
<h2><a href="/page/classlist.html">查看班级列表</a>  &nbsp;&nbsp; </h2>
<table style="border: 1px solid; width: 500px; text-align:center">
	<thead style="background:#fcf">
		<tr>
			<th>班级名称</th>
			<th>所在年级</th>
			<th>人数</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><a href="studentlist.html">春田花花班</a></td>
			<td>3年级</td>
			<td>25</td>
		</tr>
		<tr>
			<td><a href="studentlist.html">夏日绿草班</a></td>
			<td>4年级</td>
			<td>30</td>
		</tr>
	</tbody>
	
</table>
</body>
</html>