-------------------------------사원정보 테이블----------------------------------
CREATE TABLE COMPANY (
  EMPNO VARCHAR2(6) constraint PK_ENPNO primary key,    --사원번호
  NAME VARCHAR2(20) NOT NULL,                            --이름
  PASSWORD VARCHAR2(50) NOT NULL,                        --비밀번호
  GEN VARCHAR2(2),                                        --성별
  BIRTH VARCHAR2(20) NOT NULL,                           --생일
  TEL VARCHAR2(20) constraint UNIQ_TEL unique,          --전화번호
  EMAIL VARCHAR2(30),                                     --이메일
  CDEPT VARCHAR2(10) NOT NULL,                           --부서
  CRANK VARCHAR2(20) NOT NULL,                           --직급
  HIREDATE DATE,                                          --입사일
  LEAVEDATE DATE DEFAULT NULL,                           --퇴사일
  CREATEDATE DATE DEFAULT SYSDATE,                       --가입일
  IP VARCHAR2(15) NOT NULL                                --사용IP
);

-------------------------------사원정보 삽입 예제---------------------------------
INSERT INTO COMPANY VALUES('K1234', '배영민', '1234', '남' , TO_DATE('1990-07-18'), 010-1234-5764, 'qodudals90@naver.com', '기획부', '주임',
TO_DATE('2015-05-17'), NULL, NULL, '192.168.0.12');
INSERT INTO COMPANY VALUES('S1234', '배영순', '1234', '여' , TO_DATE('1994-06-22'), 010-4566-6498, 'qodudals90@naver.com', '영업부', '대리',
TO_DATE('2015-08-21','yyyy-mm-dd'), NULL, NULL, '192.168.0.45');

-------------------------------사원정보 확인--------------------------------------
DESC COMPANY;
SELECT * FROM COMPANY;

COMMIT; 
--############################################################################--




-------------------------------근태사항 테이블----------------------------------
CREATE TABLE ATTENDANCE (																		
ANUM NUMBER(3) constraint PK_ANUM123 primary key,											--일련번호
EMPNO VARCHAR2(6) NOT NULL,																	--사원번호		
DAY DATE DEFAULT TO_CHAR(SYSDATE,'YYYY-MM-DD'),											--날짜
TIME VARCHAR2(10) DEFAULT TO_CHAR(SYSDATE,'HH:MI:SS'),										--시간	
TYPE VARCHAR2(10) NOT NULL,																--근태유형
constraint FK_EMPNO123 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE
);

-------------------------------근태관리 일련번호 시퀀스----------------------------
CREATE SEQUENCE ANUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------근태관리 삽입 예제----------------------------------
INSERT INTO ATTENDANCE VALUES(ANUM_SEQ.NEXTVAL, 'P1111', TO_CHAR(SYSDATE,'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH:MI:SS'), '출근');
INSERT INTO ATTENDANCE VALUES(ANUM_SEQ.NEXTVAL, 'M1234', TO_CHAR(SYSDATE,'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH:MI:SS'), '퇴근');

-------------------------------근태관리 확인--------------------------------------
DESC ATTENDANCE;
SELECT * FROM ATTENDANCE;

COMMIT;
DROP SEQUENCE ANUM_SEQ;
delete from attendance;
--############################################################################--




-------------------------------회의실예약 테이블----------------------------------
CREATE TABLE CRR (
CNUM NUMBER(3) constraint PK_CNUM primary key,											--일련번호
EMPNO VARCHAR2(5) NOT NULL,																	--사원번호
RNUM VARCHAR2(10) NOT NULL,																	--방번호
PURPO VARCHAR2(20) NOT NULL,																--목적
RESERDATE VARCHAR2(8) NOT NULL,                             
STIME VARCHAR2(8) NOT NULL,																	--시작시간
ETIME VARCHAR2(8) NOT NULL,																	--종료시간
CONFIRMSTATE VARCHAR2(8) NOT NULL,															--예약상태
constraint FK_EMPNO2 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE,
constraint FK_RNUM foreign key (RNUM) REFERENCES CRI (RNUM) ON DELETE CASCADE
);

-------------------------------회의일예약 일련번호 시퀀스--------------------------
CREATE SEQUENCE CNUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------회의실예약 삽입 예제--------------------------------
INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, 'P1111', 'A', '회의', '20160630','09:00', '10:00','승인');
INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, 'M1234', 'B', '회의', '20160630','10:00', '12:00','대기');

-------------------------------회의실예약 확인------------------------------------
DESC CRR;
SELECT * FROM CRR;
delete from crr;
DROP SEQUENCE CNUM_SEQ;
COMMIT;
--############################################################################--




-------------------------------회의실정보 테이블----------------------------------
CREATE TABLE CRI (
RNUM VARCHAR2(10) constraint PK_RNUM primary key,											--방번호
PEOPLE NUMBER(3) NOT NULL,																	--인원수
BEAMYN VARCHAR2(1) NOT NULL,																--빔여부
MICYN VARCHAR2(1) NOT NULL,																	--마이크여부
PCYN VARCHAR2(1) NOT NULL																	--PC여부
);

-------------------------------회의실정보 삽입 예제--------------------------------
INSERT INTO CRI VALUES('A', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('B', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('C', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('D', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('E', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('F', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('G', 08, 'O', 'X', 'X');
INSERT INTO CRI VALUES('H', 08, 'O', 'X', 'X');
INSERT INTO CRI VALUES('I', 08, 'O', 'X', 'X');

-------------------------------회의실정보 확인------------------------------------
SELECT * FROM CRI;
DESC CRI;
DELETE FROM CRI;
COMMIT;
--############################################################################--




-------------------------------공지사항 테이블----------------------------------
CREATE TABLE NOTICE (
NNUM NUMBER(3) constraint PK_NUM primary key, 											--일련번호
TITLE VARCHAR2(500) NOT NULL,																--제목
CONTENT VARCHAR2(4000) NOT NULL,															--내용
EMPNO VARCHAR2(5) NOT NULL,																	--사원번호
WDATE VARCHAR2(25) DEFAULT TO_CHAR(SYSDATE,'YYYY-MM-DD HH:MI:SS'),							--작성일
constraint FK_EMPNO3 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE	
);

-------------------------------공지사항 글번호 시퀀스 -----------------------------
CREATE SEQUENCE NNUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------공지사항 삽입 예제----------------------------------
INSERT INTO NOTICE VALUES(NNUM_SEQ.NEXTVAL, '공지사항', '안녕하세요 사원관리 프로그램입니다.', 'D0002', to_char(sysdate,'YYYY-MM-DD HH:MI:SS'));
INSERT INTO NOTICE VALUES(NNUM_SEQ.NEXTVAL, '필수사항', '안녕하세요', 'P9999', to_char(sysdate,'YYYY-MM-DD HH:MI:SS'));

-------------------------------공지사항 확인-------------------------------------
DESC NOTICE;
SELECT * FROM NOTICE;
DROP SEQUENCE NNUM_SEQ;
DELETE FROM NOTICE;

COMMIT;


