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


public class ArticleController {
private HttpServletRequest request;
private HttpServletResponse response;
private Connection conn;

public ArticleController(HttpServletRequest request, HttpServletResponse response, Connection conn) {
	this.request = request;
	this.response = response;
	this.conn = conn;
}
public void showList() throws ServletException, IOException
{
	int page = 1;
	if (request.getParameter("page") != null && request.getParameter("page").length() != 0) {
		page = Integer.parseInt(request.getParameter("page"));
	}

	int itemsInAPage = 10;
	int limitFrom = (page - 1) * itemsInAPage;
	
	

	SecSql sql = SecSql.from("SELECT COUNT(*)");
	sql.append("FROM article");
	
	int totalCnt = DBUtil.selectRowIntValue(conn, sql);
	int totalPage = (int)Math.ceil(totalCnt / (double) itemsInAPage);


	sql = SecSql.from("SELECT A.*, M.name AS writerName");
	sql.append(" FROM article AS A");
	sql.append(" INNER JOIN `member` AS M");
	sql.append(" ON A.memberId = M.id");
	sql.append("ORDER BY A.id DESC");
	sql.append("LIMIT ?, ?;", limitFrom, itemsInAPage);
	
	List<Map<String, Object>> articleRows = DBUtil.selectRows(conn, sql);

	request.setAttribute("page", page);
	request.setAttribute("articleRows", articleRows);
	request.setAttribute("totalCnt", totalCnt);
	request.setAttribute("totalPage", totalPage);
	
	// list.jsp 화면으로 제어권을 넘기기. (포워딩)
	request.getRequestDispatcher("/jsp/article/list.jsp").forward(request, response);

}

public void doDelete() throws ServletException, IOException
{
	HttpSession session = request.getSession();
	Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
	
	if (loginedMemberId == null) {
	    response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
	    return;
	}
	

	
	int id = Integer.parseInt(request.getParameter("id"));
	SecSql sql = SecSql.from("SELECT * ");
	sql.append("FROM article ");
	sql.append( "WHERE id = ?", id);
	
	Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);
	
	if (articleRow == null) {
	    response.getWriter().append("<script>alert('존재하지 않는 게시글입니다.'); history.back();</script>");
	    return;
	}
	
	int articleMemberId = (int) articleRow.get("memberId");

	if (loginedMemberId != articleMemberId) {
	    response.getWriter().append("<script>alert('해당 게시글의 삭제 권한이 없습니다.'); history.back();</script>");
	    return;
	}
	
	 sql = SecSql.from("DELETE");
	sql.append("FROM article");
	sql.append("WHERE id = ?", id);

	DBUtil.delete(conn, sql);
	
	response.getWriter().append(String.format("<script>alert('%d번 글이 삭제 되었습니다'); location.replace('list')</script>", id));

}

public void showDetail() throws ServletException, IOException
{
	String idStr = request.getParameter("id");
	int id = Integer.parseInt(idStr);

	SecSql sql = SecSql.from("SELECT A.*, M.name AS writerName");
	sql.append(" FROM article AS A");
	sql.append(" INNER JOIN `member` AS M");
	sql.append(" ON A.memberId = M.id");
	sql.append(" WHERE A.id = ?", id);
	
	Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);

	request.setAttribute("articleRow", articleRow);
	request.getRequestDispatcher("/jsp/article/detail.jsp").forward(request, response);

}
public void doModify() throws ServletException, IOException
{
	HttpSession session = request.getSession();
	Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
	
	if (loginedMemberId == null) {
	    response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
	    return;
	}
	

	
	int id = Integer.parseInt(request.getParameter("id"));
	SecSql sql = SecSql.from("SELECT * ");
	sql.append("FROM article ");
	sql.append( "WHERE id = ?", id);
	
	Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);
	
	if (articleRow == null) {
	    response.getWriter().append("<script>alert('존재하지 않는 게시글입니다.'); history.back();</script>");
	    return;
	}
	
	int articleMemberId = (int) articleRow.get("memberId");

	if (loginedMemberId != articleMemberId) {
	    response.getWriter().append("<script>alert('해당 게시글의 수정 권한이 없습니다.'); history.back();</script>");
	    return;
	}
	
	String title = request.getParameter("title");
	String body = request.getParameter("body");

	sql = SecSql.from("UPDATE article");
	sql.append("SET updateDate = NOW(),");
	sql.append("title = ?,", title);
	sql.append("`body` = ?", body);
	sql.append("WHERE id = ?", id);
	
	 DBUtil.update(conn, sql);

	response.getWriter()
			.append(String.format("<script>alert('%d번 글이 수정 되었습니다'); location.replace('list')</script>", id));

}
public void doWrite() throws ServletException, IOException
{
	HttpSession session = request.getSession();
    int loginedMemberId = (int) session.getAttribute("loginedMemberId");
    
	String title = request.getParameter("title");
	String body = request.getParameter("body");

	SecSql sql = SecSql.from("INSERT INTO article");
	sql.append("SET regDate = NOW(),");
	sql.append("updateDate = NOW(),");
	sql.append("memberId = ?,", loginedMemberId);
	sql.append("title = ?,", title);
	sql.append("`body` = ?", body);

	int id = DBUtil.insert(conn, sql);

	response.getWriter()
			.append(String.format("<script>alert('%d번 글이 작성 되었습니다'); location.replace('list')</script>", id));

}
public void showModify() throws ServletException, IOException
{
	
	HttpSession session = request.getSession();
	Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
	
	if (loginedMemberId == null) {
	    response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
	    return;
	}
	

	
	int id = Integer.parseInt(request.getParameter("id"));
	SecSql sql = SecSql.from("SELECT * ");
	sql.append("FROM article ");
	sql.append( "WHERE id = ?", id);
	
	Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);
	
	if (articleRow == null) {
	    response.getWriter().append("<script>alert('존재하지 않는 게시글입니다.'); history.back();</script>");
	    return;
	}
	
	int articleMemberId = (int) articleRow.get("memberId");

	if (loginedMemberId != articleMemberId) {
	    response.getWriter().append("<script>alert('해당 게시글의 수정 권한이 없습니다.'); history.back();</script>");
	    return;
	}

	request.setAttribute("articleRow", articleRow);

	request.getRequestDispatcher("/jsp/article/modify.jsp").forward(request, response);

}
public void showWrite() throws ServletException, IOException
{
	HttpSession session = request.getSession();
    if (session.getAttribute("loginedMemberId") == null) {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
        return;
    }
	request.getRequestDispatcher("/jsp/article/write.jsp").forward(request, response);

}

}
