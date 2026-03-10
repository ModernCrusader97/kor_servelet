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
<title>게시글 상세보기</title>
</head>
<body>
	<h1>게시글 상세보기</h1>
	<br />
	<div>
		번호 :
		<%=articleRow.get("id")%>
	</div>
	<div>
		날짜 :
		<%=articleRow.get("regDate")%>
	</div>
	<div>
		제목 :
		<%=articleRow.get("title")%>
	</div>
	<div>
        작성자:
       <%= articleRow.get("writerName") %>
    </div>
	<div>
		내용 :
		<%=articleRow.get("body")%>
	</div>
	
	<div>
	<a style="color:green" href="list">리스트로 돌아가기</a>
	</div>
</body>
</html>