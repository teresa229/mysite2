package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestVo;

public class BoardDao {

//필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	//생성자
	
	//메소드-g/s
	
	//메소드-일반
	
	//DB접속
	private void getConnection() {
		
		try {
		// 1. JDBC 드라이버 (Oracle) 로딩
		Class.forName(driver);

	    // 2. Connection 얻어오기
		conn = DriverManager.getConnection(url, id, pw);
		
		} catch (ClassNotFoundException e) {
		    System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	
	}
	
	private void close() {
	   
	    // 5. 자원정리
	    try {              
	    	if (rs != null) {
	            rs.close();
	        }                
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }
	} 
	
	/* 리스트  */
	public List<BoardVo> ListAllboard(){ 
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		getConnection();
		try {
		    /*
		    select board.no,
                   users.name,
                   board.title,
                   board.content,
                   board.hit,
                   to_char(board.reg_date, 'yyyy-mm-dd'),
                   board.user_no
            from users,board
            where users.no=board.user_no
            order by reg_date desc;
		     */
			String query =  "";
			query += " select board.no, ";
			query += "        users.name, ";
			query += "        board.title, ";
			query += "        board.content,";
			query += "        board.hit, ";
			query += "        to_char(board.reg_date, 'yyyy-mm-dd') reg_date, ";
			query += "        board.user_no ";
			query += " from users,board ";
			query += " where users.no=board.user_no ";
			query += " order by reg_date desc ";
			
			pstmt = conn.prepareStatement(query);	//쿼리로 만들기
			rs = pstmt.executeQuery();
			
			//결과 처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				
				BoardVo boardVo = new BoardVo(no,name,title,content,hit,regDate,userNo);
				boardList.add(boardVo);
			}

		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return boardList;
	}
		
	/*게시판 검색 */
	public List<BoardVo> boardList(String str){
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		getConnection();
		
		try {
		    /*
            select board.no,
                   users.name,
                   board.title,
                   board.content,
                   board.hit,
                   board.reg_date,
                   board.user_no
            from users,board
            where users.no=board.user_no
            and board.title like '%시%'
            order by reg_date desc;
		     */
			String query =  "";
			query += " select board.no, ";
			query += "        users.name, ";
			query += "        board.title, ";
			query += "        board.content,";
			query += "        board.hit, ";
			query += "        board.reg_date, ";
			query += "        board.user_no ";
			query += " from users,board";
			query += " where users.no=board.user_no ";
			query += " and board.title like ? ";
			query += " order by reg_date desc ";
			
			pstmt = conn.prepareStatement(query);  //주의 :쿼리로 만들기
			
			pstmt.setString(1, "%" + str +"%" );
			
			rs = pstmt.executeQuery();
			
			//결과 처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				
				BoardVo boardVo = new BoardVo(no,name,title,content,hit,regDate,userNo);
				boardList.add(boardVo);
			}

		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return boardList;
	}
		
	/* 게시판 글 조회 */
	public BoardVo getBoard(int no) {
		
		BoardVo boardVo = null;
		getConnection();
		
		try {
		    /*
            select board.no,
                   users.name,
                   board.title,
                   board.content,
                   board.hit,
                   board.reg_date,
                   board.user_no
            from users,board
            where users.no=board.user_no
            and board.no= 10;
		     */
			String query =  "";
			query += " select board.no, ";
			query += "        users.name, ";
			query += "        board.title, ";
			query += "        board.content, ";
			query += "        board.hit, ";
			query += "        board.reg_date, ";
			query += "        board.user_no ";
			query += " from users,board ";
			query += " where users.no=board.user_no ";
			query += " and board.no= ? ";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			
			//결과 처리
			while(rs.next()) {
				int bNo = rs.getInt("no");
				String name = rs.getString("name");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				
				boardVo = new BoardVo(bNo, name, title, content, hit, regDate, userNo);
			}

		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return boardVo;
	}
	
	
	/* 조회수 업데이트 */
	public int hitUpdate(int no) {
		
		int count =0;
		
		getConnection();

		try {
		    /*
		    update board
            set hit=1
            where no=62;
		     */
			String query =  "";
			query += " update board ";
			query += " set hit= hit+1 ";
			query += " where no=? ";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1,no);

			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건 hitUpdate");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return count;
	}
	
	
    /* insert */
	public int boardInsert(BoardVo boardVo) {
		
		int count =0;
		
		getConnection();

		try {
		    /*
		    insert into board
            values(seq_guestbook_id.nextval,'제목', '내용', default, sysdate, 1);    default
		     */
			String query =  "";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, default, sysdate, ?) ";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUserNo());      //주의:getUserNo() 

			//쿼리문 실행하는 코드가 없어요
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건 insert");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return count;
	}
	
	/* modify */
	public int boardModify(BoardVo boardVo) {
		
		int count =0;
		
		getConnection();

		try {
		    /*
		    update board
            set title = '첫번째 게시물',
                content = '게시판 작업'
            where no= 1;
		     */
			String query =  "";
			query += " update board ";
			query += " set title = ?, ";
			query += "     content = ? ";
			query += " where no= ? ";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());

			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건 modify");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return count;
	}
	
	/* delete */
	public int boardDelete(int no) {
		
		int count =0;
		
		getConnection();

		try {
		    /*
		    delete from board
            where no = 61;
		     */
			
			String query =  "";
			query += " delete from board ";
			query += " where no = ? ";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건 delete");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
	    close();
	    return count;
	}
	
}
