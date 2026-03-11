<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%
List<Map<String, Object>> articleRows = (List<Map<String, Object>>) request.getAttribute("articleRows");

int cPage = (int) request.getAttribute("page"); // 현재 페이지 번호
int totalCnt = (int) request.getAttribute("totalCnt"); // 전체 게시글 수
int totalPage = (int) request.getAttribute("totalPage"); // 계산된 총 페이지 수

Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
String loginedMemberName = (String) session.getAttribute("loginedMemberName");

boolean isLogined = (loginedMemberId != null);
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>

<style>
table>thead>tr>th, table>tbody>tr>td {
	padding: 10px;
}
</style>
</head>
<body>
	<a href="../home/main">메인으로 이동</a>
	<h1>게시글 목록</h1>
		<%
	if (isLogined) {
	%>
	<div>
		현재
	<%=loginedMemberName%>(<%=loginedMemberId%>)님 로그인 중
	</div>
	<div>
		<a href="../member/doLogout">로그아웃</a>
	</div>
	<%
	}
	%>
	<%
	if (!isLogined) {
	%>
	<div>
	 
	</div>
	<div>
		<a href="../member/login">로그인</a>
	</div>
	<%
	}
	%>
	총 게시글 갯수 :
	<%=totalCnt%>
	<% if (isLogined) { %>
    <div style="margin: 10px 0;">
        <a href="write" style="display: inline-block; padding: 5px 10px; background-color: lightgray; text-decoration: none; color: black; border: 1px solid gray; border-radius: 3px;">글쓰기</a>
    </div>
	<% } %>
	<table border="1"
		style="border-collapse: collapse; border-color: green">
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>제목</th>
				<th>내용</th>								
				<th>작성자</th>
				<th>삭제</th>
				<th>수정</th>
				
			</tr>
		</thead>
		<tbody>
			<%
			for (Map<String, Object> articleRow : articleRows) {
			%>
			<tr style="text-algin: center;">
				<td><%=articleRow.get("id")%>번</td>
				<td><%=articleRow.get("regDate")%></td>

				<td><a href="detail?id=<%=articleRow.get("id")%>"><%=articleRow.get("title")%></a></td>

				<td><%=articleRow.get("body")%></td>
				<td><%=articleRow.get("writerName")%></td>
				<% 
				Object writerIdObj = articleRow.get("memberId");  
				boolean isWriter = (loginedMemberId != null && writerIdObj != null && loginedMemberId.equals(writerIdObj));
				%>
				<% if (isWriter) { %>
				<td><a
					onclick="if(confirm('정말 삭제하시겠습니까?') == false) {return false;}"
					href="doDelete?id=<%=articleRow.get("id")%>">delete</a></td>				
				<td><a
					onclick="if(confirm('수정하시겠습니까?') == false) {return false;}"
					href="modify?id=<%=articleRow.get("id")%>">edit</a></td>

				<% } else { %>
     			 <td>-</td>
       			 <td>-</td>
   				 <% } %>
			</tr>
			<%
			}
			%>

		</tbody>
	</table>
	<style type="text/css">
.page {
	font-size: 1.4rem;
}

.page>a {
	color: black;
	text-decoration: none;
}

.page>a.cPage {
	color: red;
	text-decoration: underline;
}
</style>
	<div class="page">
		<%
		for (int i = 1; i <= totalPage; i++) {
		%>
		<a class="<%=cPage == i ? "cPage" : ""%>" href="list?page=<%=i%>"><%=i%></a>
		<%
		}
		%>
	</div>
</body>
</html>