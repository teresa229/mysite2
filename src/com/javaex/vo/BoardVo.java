package com.javaex.vo;

public class BoardVo {

	//필드 : private
	private int no;
	private String name;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	
	//생성자
	public BoardVo() {}
		
	public BoardVo(int no, String title, String content) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
	}
	
	public BoardVo(String title, String content, int userNo) {
		super();
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}

	public BoardVo(int no, String name, String title, String content, int hit, String regDate, int userNo) {
		super();
		this.no = no;
		this.name = name;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
	}
	
	//메소드
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", name=" + name + ", title=" + title + ", content=" + content + ", hit=" + hit
				+ ", regDate=" + regDate + ", userNo=" + userNo + "]";
	}

}
