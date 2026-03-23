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
private ArticleService articleService;

private boolean isLogined() throws IOException {
    HttpSession session = request.getSession();
    if (session.getAttribute("loginedMemberId") == null) {
        
        response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
        return false;
    }
    return true;
}

private int getLoginedMemberId() {
	return (int) request.getSession().getAttribute("loginedMemberId");
}


private Map<String, Object> getArticleById(int id) throws IOException {

	Map<String, Object> articleRow = articleService.getArticleById(id);

    if (articleRow == null) {        
        response.getWriter().append("<script>alert('존재하지 않는 게시글입니다.'); history.back();</script>");
        return null;
    }
    return articleRow;
}

private boolean hasPermission(Map<String, Object> articleRow) throws IOException {

    Integer loginedMemberId = getLoginedMemberId();
    int articleMemberId = (int) articleRow.get("memberId");

    if (!isLogined() || loginedMemberId != articleMemberId) 
           return false;

    return true;
}

public ArticleController(HttpServletRequest request, HttpServletResponse response, Connection conn) {
	this.request = request;
	this.response = response;
	this.conn = conn;
	this.articleService = new ArticleService(conn);
}

public void showList() throws ServletException, IOException
{
	int page = 1;
	if (request.getParameter("page") != null && request.getParameter("page").length() != 0) {
		page = Integer.parseInt(request.getParameter("page"));
	}

	int itemsInAPage = 10;
	int totalCnt = articleService.getTotalCnt();
	int totalPage = (int)Math.ceil(totalCnt / (double) itemsInAPage);
	var articleRows = articleService.getForPrintArticles(page, itemsInAPage);
    
	request.setAttribute("page", page);
	request.setAttribute("articleRows", articleRows);
	request.setAttribute("totalCnt", totalCnt);
	request.setAttribute("totalPage", totalPage);
	
	// list.jsp 화면으로 제어권을 넘기기. (포워딩)
	request.getRequestDispatcher("/jsp/article/list.jsp").forward(request, response);

}

public void doDelete() throws ServletException, IOException
{

	if (isLogined() == false)
		 return;

	
	int id = Integer.parseInt(request.getParameter("id"));

	Map<String, Object> articleRow = getArticleById(id);
	
	if (articleRow == null) 
		return;

	

	if ( !hasPermission(articleRow)) {
	    response.getWriter().append("<script>alert('해당 게시글의 삭제 권한이 없습니다.'); history.back();</script>");
	    return;
	}
	
	if( articleService.delete(id) == 0) {
	    response.getWriter().append("<script>alert('삭제에 실패하였습니다.'); history.back();</script>");
	    return;
	}
	
	response.getWriter().append(String.format("<script>alert('%d번 글이 삭제 되었습니다'); location.replace('list')</script>", id));

}

public void showDetail() throws ServletException, IOException
{
	String idStr = request.getParameter("id");
	int id = Integer.parseInt(idStr);

	Map<String, Object> articleRow = articleService.getForPrintArticleById(id);
	
	if (articleRow == null) {
        response.getWriter().append(String.format("<script>alert('%d번 게시글은 존재하지 않습니다.'); history.back();</script>", id));
        return;
    }
	
	request.setAttribute("articleRow", articleRow);
	request.getRequestDispatcher("/jsp/article/detail.jsp").forward(request, response);

}

public void doModify() throws ServletException, IOException
{
	
	if (isLogined() == false) 
		return;


	
	int id = Integer.parseInt(request.getParameter("id"));

	Map<String, Object> articleRow = getArticleById(id);
	
	if (articleRow == null) 
		return;


	if ( !hasPermission(articleRow)) {
	    response.getWriter().append("<script>alert('해당 게시글의 수정 권한이 없습니다.'); history.back();</script>");
	    return;
	}
	
	String title = request.getParameter("title");
	String body = request.getParameter("body");

	

	if (articleService.modify(id, title, body) == 0) {
	    response.getWriter().append("<script>alert('수정에 실패하였습니다.'); history.back();</script>");
	    return;
	}
	
	response.getWriter()
			.append(String.format("<script>alert('%d번 글이 수정 되었습니다'); location.replace('list')</script>", id));

}

public void doWrite() throws ServletException, IOException
{

	
	if (isLogined() == false) 
		return;

    int loginedMemberId = getLoginedMemberId();
    
	String title = request.getParameter("title");
	String body = request.getParameter("body");

    int id = articleService.write(loginedMemberId, title, body);
    
	response.getWriter()
			.append(String.format("<script>alert('%d번 글이 작성 되었습니다'); location.replace('list')</script>", id));

}

public void showModify() throws ServletException, IOException
{

	if (isLogined() == false)
		 return;

	

	
	int id = Integer.parseInt(request.getParameter("id"));

	
	Map<String, Object> articleRow = getArticleById(id);
	
	if (articleRow == null)
		return;
		

	if (!hasPermission(articleRow)) {
	    response.getWriter().append("<script>alert('해당 게시글의 수정 권한이 없습니다.'); history.back();</script>");
	    return;
	}

	request.setAttribute("articleRow", articleRow);

	request.getRequestDispatcher("/jsp/article/modify.jsp").forward(request, response);

}
public void showWrite() throws ServletException, IOException
{
    if (!isLogined()) 
        return;
	request.getRequestDispatcher("/jsp/article/write.jsp").forward(request, response);

}

}
