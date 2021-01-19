package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestVo;

@WebServlet("/guestbook")
public class GuestbookController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GuestbookController");
		
		String action = request.getParameter("action");
		System.out.println("action");
	       
		if("guestList".equals(action)) {
			System.out.println("방명록 리스트");
			
			//리스트 조회
			GuestDao guestDao = new GuestDao();
			List<GuestVo> guestList = guestDao.ListAllGuest();
			
			/* attribute */ //데이터 전달
			request.setAttribute("gList", guestList);
			
			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			
		} else if("add".equals(action)) {
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			GuestVo guestVo = new GuestVo(name, password, content);
			
			/* insert */
			GuestDao guestDao = new GuestDao();
			guestDao.guestInsert(guestVo);
			
			/* redirect */
			WebUtil.redirect(request, response, "/mysite2/guestbook?action=guestList"); //고민
			
		} else if("delete".equals(action)) {
			System.out.println("삭제");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass"); //pass 주의
			
			GuestVo guestVo = new GuestVo(no, password);
			
			/* delete */
			GuestDao guestDao = new GuestDao();
		/*	guestDao.guestDelete(guestVo);
			
			* redirect *
			WebUtil.redirect(request, response, "/mysite2/guestbook?action=guestList");
		*/	
			
			/* 성공:결과 */
			int count = guestDao.guestDelete(guestVo);
			
			if(count==1) { //성공
				WebUtil.redirect(request, response, "/mysite2/guestbook?action=guestList");
			}else {        //실패
				//deleteForm 화면 - 틀린 문구 보여주기 + count값 보내준다.
				request.setAttribute("result", count);
				
				WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
				
			}

		} else if("guestDelete".equals(action)) {
			System.out.println("방명록 삭제 폼");

			/* forward */
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
