package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	    //필드
		/* import */
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;
		
		private String driver = "oracle.jdbc.driver.OracleDriver";
		private String url = "jdbc:oracle:thin:@localhost:1521:xe";
		private String id = "webdb";        /* *********** 너무너무 중요함 *********** */ 
		private String pw = "webdb";        /* *********** 너무너무 중요함 *********** */
		
		//생성자
		//메소드-g/s
		//메소드-일반
		
		/* DB접속 */
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
		
		/* 5. 자원정리 */
		private void close() {
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
		
		/* insert */
		public int insert(UserVo userVo) { //void를 숫자로 바꿔 주기...-> int, 묶어서 하나로 받기.
			int count = 0;
			getConnection();
			
			try { 
				/*
				insert into users
				values(seq_users_no.nextval,
				                      'aaa',
				                     '1234',
			                       	  '이름',
				                      '성별'
				                     );
				*/
				
				//3. sql문 준비/ 바인딩/ 실행
				String query = "";
				query += " insert into users ";
				query += " values (seq_users_no.nextval, ";
				query += "        ?, ";        /* *********** 너무너무 중요함 *********** */
				query += "        ?, ";        // 자바열 + 문자열" "
				query += "        ?, ";        // 1)따옴표 없애기, 2)컴마 주의하기, 3);은 넣지 않는다.
				query += "        ? ";
				query += " ) ";
				
				pstmt = conn.prepareStatement(query);     /* 쿼리로 만들기 */
				pstmt.setString(1, userVo.getId());       //?(물음표) 중 1번쨰, 순서 중요
				pstmt.setString(2, userVo.getPassword()); //?(물음표) 중 2번쨰, 순서 중요
				pstmt.setString(3, userVo.getName());	  //?(물음표) 중 3번쨰, 순서 중요
				pstmt.setString(4, userVo.getGender());   //?(물음표) 중 4번쨰, 순서 중요
				
				count = pstmt.executeUpdate();            //쿼리문 실행 : 성공한 갯수를 count로 표현
				
				// 4. 결과처리 
				System.out.println("insert"+ count + " 건 회원정보 저장");
				
			} catch (SQLException e) {                                                 
			    System.out.println("error:" + e);                                      
			}            
			
			close();
			return count;
			
		}
		
		/* getUser :id/pw */  //로그인할 때 유저저장용
		public UserVo getUser(String id, String pw) {
			UserVo userVo = null;               //값이 없을 경우 null로 받을 수 있게 만들어 준다. 바로 빠져나오면 없어지므로 {}밖으로 빼준다.
			
			getConnection();                    //접속
			
			try {
				String query = "";
				query += " select no, ";
				//query += "        id, ";      //필요없는 부분 - 밑에서 어차피 받음.
				//query += "        password,"; //필요없는 부분 - 밑에서 어차피 받음.
				query += "        name ";
				//query += "        gender ";   //필요없는 부분
				query += " from users ";
				query += " where id =? ";
				query += " and password = ? ";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, id); 
				pstmt.setString(2, pw); 
				
				//count = pstmt.executeUpdate(); //select문이기 때문에 쿼리문.
	/**/		rs = pstmt.executeQuery(); //query실행, rs로 결과를 받아온다.
				
				//결과처리
				while(rs.next()) { //rs.형식으로 오기때문에 다 꺼내서 userVo에 넣었다.
					int no = rs.getInt("no");
					String name = rs.getString("name");
					
					userVo = new UserVo(no, name);  //가로빠져나가면 안되지. 결과 담을 vo, (통과되는 2개 값만 필요);
				}
				
			} catch (SQLException e) {                                                 
			    System.out.println("error:" + e);                                      
			}            
			close();
			return userVo;			
		} 
		
		/* getUser */ //유저정보 가져오기 메소드(회원정보 수정시 사용) -getUser 같은 이름이어도 괜찮음. 오버로딩../**/
		public UserVo getUser(int no) {
			UserVo userVo = null;
			getConnection();
			
			try {
					/*
					select no,
					       id,
					       password,
					       name,
					       gender
					from users
					where no =1; 
					*/
				
					String query ="";
					query +=" select no,";
					query +="        id, ";
					query +="        password, ";
					query +="        name, ";
					query +="        gender ";
					query +=" from users ";
					query +=" where no = ? ";
					
					pstmt = conn.prepareStatement(query); //쿼리 만들기
					
					pstmt.setInt(1, no);
					
					rs = pstmt.executeQuery();            //쿼리 실행하기
					
					while(rs.next()) {
						int userno = rs.getInt("no");
						String id = rs.getString("id");
						String pw = rs.getString("password");
						String name = rs.getString("name");
						String gender = rs.getString("gender");
						
						userVo = new UserVo(userno, id, pw, name,gender);
					}
				
				} catch (SQLException e) {                                                 
				    System.out.println("error:" + e);                                      
				}            
				close();
				return userVo;
		}
		
			
		public int userUpdate(UserVo updvo) {
			int count = 0;
			
			getConnection();
			
			try {
				/*
				update users
				set password = '1234',
				    name = '홍길동',
				    gender = 'male'
				where no = 1;
				*/
				
				String query = "";
				query += " update users      ";
				query += " set password = ?, ";
				query += "     name = ?,     ";
				query += "     gender = ?    ";
				query += " where no = ?      ";
				
				pstmt = conn.prepareStatement(query);	//쿼리로 만들기 : prepareStatement(query)
				
				pstmt.setString(1, updvo.getPassword());
				pstmt.setString(2, updvo.getName());
				pstmt.setString(3, updvo.getGender());
				pstmt.setInt(4, updvo.getNo());
				
				//결과처리
				count = pstmt.executeUpdate();         //executeUpdate()
				System.out.println("[ " + count + " 건 수정]");
				
			} catch (SQLException e) {
			    System.out.println("error:" + e);
			}
			
			close();
			return count;
		}
		
}
