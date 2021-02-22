CREATE TABLE MEMBERS(
	UserIdx	NUMBER(4) PRIMARY KEY,
	UserId	varchar2(100),
	UserPw	varchar2(150),
	UserName	varchar2(100),
	UserPhone	varchar2(100),
	UserGenre1	varchar2(100),
	UserGenre2	varchar2(100)
)

CREATE TABLE STEAMLIST(
	STEAMIdx	NUMBER(5) PRIMARY KEY,
	UserIdx	NUMBER(4) REFERENCES MEMBERS(useridx), 
	songname	varchar2(150),
	artist	varchar2(100),
	album	varchar2(100),
	songlink	varchar2(200)
)