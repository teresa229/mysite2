package com.javaex.dao; //서버 켤필요 없다. java

import com.javaex.vo.UserVo;

public class UserDaoTest {

	public static void main(String[] args) {
		
		UserDao userDao = new UserDao();
		UserVo userVo = userDao.getUser("aaa", "1234");  //임의의 (id,pw)값을 넣어준다.
		
		System.out.println(userVo); //틀린값이면 null이 나온다.

	}

}
