<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%
Map<String, Object> articleRow = (Map<String, Object>) request.getAttribute("articleRow");
%>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<style>
table>thead>tr>th, table>tbody>tr>td {
	padding: 10px;
}
</style>
</head>
<body>
	<a href="../home/main">메인으로 이동</a>
	<h1>게시글 수정</h1>

	<form action="doModify" method="POST">
		<input type="hidden" name="id" value="<%= articleRow.get("id") %>" />
<div>
            <input name="title" type="text" value="<%= articleRow.get("title") %>" placeholder="제목 입력" />
        </div>
        <div>
            <input name="body" type="text" value="<%= articleRow.get("body") %>" placeholder="제목 입력" />
        </div>
        <div>
            <input type="submit" value="수정 완료" />
        </div>
	</form>
</body>
</html>