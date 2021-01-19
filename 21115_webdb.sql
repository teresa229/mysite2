/* board */
drop table board; --예전 작업 삭제
drop sequence seq_board_no; --예전 시퀀스 삭제

--테이블 생성
create table board(
      no number, --pk
      title varchar2(500) not null ,
      content varchar2(4000),
      hit number default 0,
      reg_date date not null,
      user_no number not null,
      primary key(no),
      constraint board_fk foreign key(user_no)
      references users(no)
      );
        
--board 시퀀스 생성
create sequence seq_board_no
increment by 1
start with 1;
nocache;    

--조회
select*
from board;

select*
from users;

select*
from users,board;

--테이블 조회
select no,
       title,
       content,
       hit,
       reg_date,
       user_no
from board;

--insert
insert into board
values(seq_guestbook_id.nextval,'게시글', '게시내용1', default, sysdate, 1);

insert into board
values(seq_guestbook_id.nextval,'게시글2', '게시내용2', default, sysdate, 1);

--update
update board
set title = '첫번째 게시물',
    content = '게시판 작업'
where no= 1;

--delete
delete from board
where no = 61;

--조회수 업데이트
update board
set hit =1
where no=62;

--전체 목록 조회
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

--게시판 글 조회
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

--검색 like
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