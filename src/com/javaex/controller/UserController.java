package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UserController");
		
		String action = request.getParameter("action");
		System.out.println("action="+action);

		/* joinForm */
		if("joinForm".equals(action)){
		  System.out.println("회원가입 폼");
		  WebUtil.forward(request,response, "/WEB-INF/views/user/joinForm.jsp");
		
		/* join */
		} else if("join".equals(action)){
			System.out.println("회원가입");
			
			//dao -> insert 저장
			
			//http://localhost:8088/mysite2/user? uid=kk & pw=a & username=a & gender=male & action=join 
			                                                       //join은 앞에서 꺼내서 꺼내올 필요가 없다. 21
			/* 파라미터 값 꺼내기 */
			String id = request.getParameter("uid");         //uid=kk
			String password = request.getParameter("pw");    //pw=a
			String name = request.getParameter("username");  //username=a
			String gender = request.getParameter("gender");  //gender=male
			
			/* vo로 묶기 */ //--> UserVo만들기, 생성자 추가, 꺼낸값 넣기: id, password, name, gender
			UserVo userVo = new UserVo(id, password, name, gender);  //-> import: control + shift + o
			System.out.println(userVo.toString());
			
			/* insert  */ //dao클래스 insert(vo) 사용 -> 저장 -> 회원가입  : dao를 vo에 올리기
			UserDao userDao = new UserDao(); //-> import: control + shift + o
			userDao.insert(userVo); //41의 userVo
			
			/* forward */ //-> joinOK.jsp //join 만들기
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
	
		/* loginForm */
		} else if("loginForm".equals(action)){
			System.out.println("로그인 폼");
			
			/* forward */ // -> loginForm.jsp
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		} else if("login".equals(action)){
			System.out.println("로그인");
			//파라미터 id pw
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			
			//dao --> getUser();	
		    UserDao userDao = new UserDao();
		    UserVo authVo = userDao.getUser(id, pw); //2개의 값이 있어야 확인이 가능하다. dao에class를 만든다. 이름은 UserDaoTest.main체크
		                                             //Ox1234가 생성된 것이다... vo ->authVo으로 바꾸어 헷갈리지 않게 설명 편하게 바꿈.
		    System.out.println(authVo); //id pw --> 원하는 데이터 no, name
		    
			    if(authVo == null) { //로그인 실패
			    	System.out.println("로그인 실패");
			    	/* redirect--> 로그인 폼에 result파라미터를 가지고 가라*/
			    	WebUtil.redirect(request, response, "/mysite2/user?action=loginForm&result=fail");
			    	
			    } else { 
			    	System.out.println("로그인 성공");
			    	//세션영역에 필요한 값(vo) 을 넣어준다. --실제 로그인을 하게 만든다.
			    	/* 성공일 경우 */
				    HttpSession session = request.getSession(); //메모리에 있는 주소를 넣어준다. 외워도 될정도로 많이 쓰임
				    session.setAttribute("authUser", authVo);   //session의 setAttribute영역...requset의 setAttribute와 다르다.
				    
				    WebUtil.redirect(request, response, "/mysite2/main"); //로그인 된 MAIN 정우성님 생김-  1:10:30
			    }
		    	
		    } else if("logout".equals(action)){
		    	System.out.println("로그아웃");
		    	
		    	//세션영역에 있는 vo를 삭제해야함
		    	HttpSession session = request.getSession(); //미리 만들어놓은 것이다. 
		    	session.removeAttribute("authUser");
		    	session.invalidate();                     //같이 써주는 것이다.
		    	
		    	WebUtil.redirect(request, response, "/mysite2/main");
		    	
		    } else if("modifyForm".equals(action)) { 
		    	System.out.println("회원정보수정  폼");
				
				//세션영역에 있는 no를 가져오기
				HttpSession session = request.getSession();                 //Session() 정보를 불러온다.
				UserVo authUser = (UserVo)session.getAttribute("authUser"); //Session의 Attribute  //성공이름:"authUser" ///모양 UserVo
				                                                            //형변환 -> 주소를 받았다.
				//login하지 않으면 오류가 난다.
				int no = authUser.getNo();
				
				//회원정보 가져오기 
				UserDao userDao = new UserDao();
				UserVo userVo = userDao.getUser(no); //userVo주소만 있을 뿐. 내용이 실제로 들어가 있는 것은 아니다.
				
				System.out.println("getUser(no)-->" + userVo);
				
				//userVo 전달 포워드
				request.setAttribute("userVo", userVo);
								
				/* forward */
				WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
				
		    } else if("modify".equals(action)) {
		    	System.out.println("회원정보수정");
		    	
				/* Parameter */ //파라미터 값 가져오기
				String password = request.getParameter("pw");
				String name = request.getParameter("name");
				String gender = request.getParameter("gender");
				
				//확인용 : System.out.println(password + name + gender);
				
				//세션에서 no가져오기
				HttpSession session = request.getSession();
				UserVo authUser = (UserVo)session.getAttribute("authUser");
				int no = authUser.getNo();
				
				UserVo userVo = new UserVo(no, password, name, gender);
				//UserVo userVo = new UserVo(no, "", password, name, gender); 위험한 방법이라 비추하는 방법
				
				System.out.println(userVo);
				
				/* userUpdate */ //실행
				UserDao userDao = new UserDao();
				userDao.userUpdate(userVo);
				
				//session 정보도 update해주어야 한다. 내용을 수정해도 기존 db의 내용이 나간다.(황일영)
				//session의 name값만 변경하면 된다.
				authUser.setName(name);  //체크해볼것
				
				WebUtil.redirect(request, response, "/mysite2/main");
		    }
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
