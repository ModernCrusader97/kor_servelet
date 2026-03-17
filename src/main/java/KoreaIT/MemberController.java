package KoreaIT;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import KoreaIT.SecSql;
import KoreaIT.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class MemberController {
private HttpServletRequest request;
private HttpServletResponse response;
private Connection conn;

public MemberController(HttpServletRequest request, HttpServletResponse response, Connection conn) {
	this.request = request;
	this.response = response;
	this.conn = conn;
}

public void showLogin() throws ServletException, IOException
{
	request.getRequestDispatcher("/jsp/member/login.jsp").forward(request, response);

}

public void doLogin() throws ServletException, IOException
{
	String loginId = request.getParameter("loginId");
	String loginPw = request.getParameter("loginPw");
	
	SecSql sql = SecSql.from("SELECT *");
    sql.append("FROM `member` ");
    sql.append("WHERE loginId = ?", loginId);

    Map<String, Object> memberRow = DBUtil.selectRow(conn, sql);
	
    if (memberRow.isEmpty()) {
        response.getWriter().append("<script>alert('존재하지 않는 아이디입니다.'); history.back();</script>");
        return;
    }
    
    if (memberRow.get("loginPw").equals(loginPw) == false) {
        response.getWriter().append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
        return;
    }
    String name = (String) memberRow.get("name");
    
    // 로그인 성공 처리
    HttpSession session = request.getSession();
    session = request.getSession();
    session.setAttribute("loginedMemberId", memberRow.get("id"));
    session.setAttribute("loginedMemberName", name);
	response.getWriter()
			.append(String.format("<script>alert('%s님, 반갑습니다!'); location.replace('../home/main')</script>", name));

}
public void doLogout() throws ServletException, IOException
{
	HttpSession session = request.getSession();
	session.removeAttribute("loginedMemberId");
	session.removeAttribute("loginedMemberName");
    
    response.getWriter()
    .append("<script>alert('로그아웃 되었습니다.'); location.replace('../home/main');</script>");

}

public void showJoin() throws ServletException, IOException
{
	request.getRequestDispatcher("/jsp/member/join.jsp").forward(request, response);

}
public void doJoin() throws ServletException, IOException
{
	String loginId = request.getParameter("loginId");
	String loginPw = request.getParameter("loginPw");
	String name = request.getParameter("name");
	
	SecSql sql = SecSql.from("SELECT COUNT(*)");
	sql.append("FROM `member` ");
	sql.append("WHERE loginId = ?", loginId);

	
	int count =	DBUtil.selectRowIntValue(conn, sql);

	if (count > 0) {
	    response.getWriter().append(String.format("<script>alert('%s(은)는 이미 사용 중인 아이디입니다.'); history.back();</script>", loginId));
	return;
	}
	
	sql = SecSql.from("INSERT INTO member");
	sql.append("SET regDate = NOW(),");
	sql.append("updateDate = NOW(),");
	sql.append("loginId = ?,", loginId );
	sql.append("loginPw = ?,", loginPw);
	sql.append("name = ?", name);

	int id = DBUtil.insert(conn, sql);

	response.getWriter()
			.append(String.format("<script>alert('%s님, 환영합니다!'); location.replace('../home/main')</script>", name));

}

}
