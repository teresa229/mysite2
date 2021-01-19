package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardController");
		
		//post일 경우 한글 깨짐 방지
		response.setContentType("text/html;charset=utf-8");
		
		/* action */
		String action = request.getParameter("action");
		System.out.println(action);
		
		if("list".equals(action)) {
			System.out.println("게시판 리스트");
			//리스트 조회
			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.ListAllboard();
			
			/* attribute */ //데이터 전달
			request.setAttribute("bList", boardList);
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		} else if("read".equals(action)) {
			System.out.println("게시판 읽기");
			//System.out.println("no" + Integer.parseInt(request.getParameter("no")));
			int no = Integer.parseInt(request.getParameter("no"));
			
			//게시판 조회
			BoardDao boardDao = new BoardDao();
			
			//조회수 업데이트
			boardDao.hitUpdate(no);
			
			BoardVo boardVo = boardDao.getBoard(no);
			
			/* attribute */ //데이터 전달
			request.setAttribute("bVo", boardVo);
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		
		} else if("writeBoard".equals(action)) {
			System.out.println("게시판 글쓰기 폼");
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			
		} else if("write".equals(action)) {
			System.out.println("게시판 글쓰기");
			
			/* sesssion no가져오기 */
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser"); //주의하자. UserVo
			int userNo = authUser.getNo();
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardVo boardVo = new BoardVo(title, content, userNo);
			BoardDao boardDao = new BoardDao();
			
			/* insert */
			boardDao.boardInsert(boardVo);
			
			/* redirect */
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("modifyBoard".equals(action)) { 
			System.out.println("게시판 수정 폼");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			//게시판 데이터 조회
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);
			
			/* attribute */ //데이터 전달
			request.setAttribute("bVo", boardVo);
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/board/modiForm.jsp");
			
		} else if("modify".equals(action)) {
			System.out.println("게시판 수정하기");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = new BoardVo(no, title, content);
					
			BoardDao boardDao = new BoardDao();
			boardDao.boardModify(boardVo);
			
			/* redirect */
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("delete".equals(action)) {
			System.out.println("게시판 삭제");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardDelete(no);
			
			/* redirect */
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("search".equals(action)) {
			System.out.println("게시판 검색");
			
			String str = request.getParameter("searchT");
			
			/* boardList : 검색 리스트 */
			BoardDao boardDao = new BoardDao();
			List<BoardVo> searchList = boardDao.boardList(str);
			
			/* Attribute : 데이터 전송 */
			request.setAttribute("sList", searchList);
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}
			
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
