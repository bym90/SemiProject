-------------------------------������� ���̺�----------------------------------
CREATE TABLE COMPANY (
  EMPNO VARCHAR2(6) constraint PK_ENPNO primary key,    --�����ȣ
  NAME VARCHAR2(20) NOT NULL,                            --�̸�
  PASSWORD VARCHAR2(50) NOT NULL,                        --��й�ȣ
  GEN VARCHAR2(2),                                        --����
  BIRTH VARCHAR2(20) NOT NULL,                           --����
  TEL VARCHAR2(20) constraint UNIQ_TEL unique,          --��ȭ��ȣ
  EMAIL VARCHAR2(30),                                     --�̸���
  CDEPT VARCHAR2(10) NOT NULL,                           --�μ�
  CRANK VARCHAR2(20) NOT NULL,                           --����
  HIREDATE DATE,                                          --�Ի���
  LEAVEDATE DATE DEFAULT NULL,                           --�����
  CREATEDATE DATE DEFAULT SYSDATE,                       --������
  IP VARCHAR2(15) NOT NULL                                --���IP
);

-------------------------------������� ���� ����---------------------------------
INSERT INTO COMPANY VALUES('K1234', '�迵��', '1234', '��' , TO_DATE('1990-07-18'), 010-1234-5764, 'qodudals90@naver.com', '��ȹ��', '����',
TO_DATE('2015-05-17'), NULL, NULL, '192.168.0.12');
INSERT INTO COMPANY VALUES('S1234', '�迵��', '1234', '��' , TO_DATE('1994-06-22'), 010-4566-6498, 'qodudals90@naver.com', '������', '�븮',
TO_DATE('2015-08-21','yyyy-mm-dd'), NULL, NULL, '192.168.0.45');

-------------------------------������� Ȯ��--------------------------------------
DESC COMPANY;
SELECT * FROM COMPANY;

COMMIT; 
--############################################################################--




-------------------------------���»��� ���̺�----------------------------------
CREATE TABLE ATTENDANCE (																		
ANUM NUMBER(3) constraint PK_ANUM123 primary key,											--�Ϸù�ȣ
EMPNO VARCHAR2(6) NOT NULL,																	--�����ȣ		
DAY DATE DEFAULT TO_CHAR(SYSDATE,'YYYY-MM-DD'),											--��¥
TIME VARCHAR2(10) DEFAULT TO_CHAR(SYSDATE,'HH:MI:SS'),										--�ð�	
TYPE VARCHAR2(10) NOT NULL,																--��������
constraint FK_EMPNO123 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE
);

-------------------------------���°��� �Ϸù�ȣ ������----------------------------
CREATE SEQUENCE ANUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------���°��� ���� ����----------------------------------
INSERT INTO ATTENDANCE VALUES(ANUM_SEQ.NEXTVAL, 'P1111', TO_CHAR(SYSDATE,'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH:MI:SS'), '���');
INSERT INTO ATTENDANCE VALUES(ANUM_SEQ.NEXTVAL, 'M1234', TO_CHAR(SYSDATE,'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH:MI:SS'), '���');

-------------------------------���°��� Ȯ��--------------------------------------
DESC ATTENDANCE;
SELECT * FROM ATTENDANCE;

COMMIT;
DROP SEQUENCE ANUM_SEQ;
delete from attendance;
--############################################################################--




-------------------------------ȸ�ǽǿ��� ���̺�----------------------------------
CREATE TABLE CRR (
CNUM NUMBER(3) constraint PK_CNUM primary key,											--�Ϸù�ȣ
EMPNO VARCHAR2(5) NOT NULL,																	--�����ȣ
RNUM VARCHAR2(10) NOT NULL,																	--���ȣ
PURPO VARCHAR2(20) NOT NULL,																--����
RESERDATE VARCHAR2(8) NOT NULL,                             
STIME VARCHAR2(8) NOT NULL,																	--���۽ð�
ETIME VARCHAR2(8) NOT NULL,																	--����ð�
CONFIRMSTATE VARCHAR2(8) NOT NULL,															--�������
constraint FK_EMPNO2 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE,
constraint FK_RNUM foreign key (RNUM) REFERENCES CRI (RNUM) ON DELETE CASCADE
);

-------------------------------ȸ���Ͽ��� �Ϸù�ȣ ������--------------------------
CREATE SEQUENCE CNUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------ȸ�ǽǿ��� ���� ����--------------------------------
INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, 'P1111', 'A', 'ȸ��', '20160630','09:00', '10:00','����');
INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, 'M1234', 'B', 'ȸ��', '20160630','10:00', '12:00','���');

-------------------------------ȸ�ǽǿ��� Ȯ��------------------------------------
DESC CRR;
SELECT * FROM CRR;
delete from crr;
DROP SEQUENCE CNUM_SEQ;
COMMIT;
--############################################################################--




-------------------------------ȸ�ǽ����� ���̺�----------------------------------
CREATE TABLE CRI (
RNUM VARCHAR2(10) constraint PK_RNUM primary key,											--���ȣ
PEOPLE NUMBER(3) NOT NULL,																	--�ο���
BEAMYN VARCHAR2(1) NOT NULL,																--������
MICYN VARCHAR2(1) NOT NULL,																	--����ũ����
PCYN VARCHAR2(1) NOT NULL																	--PC����
);

-------------------------------ȸ�ǽ����� ���� ����--------------------------------
INSERT INTO CRI VALUES('A', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('B', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('C', 20, 'O', 'O', 'O');
INSERT INTO CRI VALUES('D', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('E', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('F', 12, 'O', 'O', 'X');
INSERT INTO CRI VALUES('G', 08, 'O', 'X', 'X');
INSERT INTO CRI VALUES('H', 08, 'O', 'X', 'X');
INSERT INTO CRI VALUES('I', 08, 'O', 'X', 'X');

-------------------------------ȸ�ǽ����� Ȯ��------------------------------------
SELECT * FROM CRI;
DESC CRI;
DELETE FROM CRI;
COMMIT;
--############################################################################--




-------------------------------�������� ���̺�----------------------------------
CREATE TABLE NOTICE (
NNUM NUMBER(3) constraint PK_NUM primary key, 											--�Ϸù�ȣ
TITLE VARCHAR2(500) NOT NULL,																--����
CONTENT VARCHAR2(4000) NOT NULL,															--����
EMPNO VARCHAR2(5) NOT NULL,																	--�����ȣ
WDATE VARCHAR2(25) DEFAULT TO_CHAR(SYSDATE,'YYYY-MM-DD HH:MI:SS'),							--�ۼ���
constraint FK_EMPNO3 foreign key (EMPNO) REFERENCES COMPANY(EMPNO) ON DELETE CASCADE	
);

-------------------------------�������� �۹�ȣ ������ -----------------------------
CREATE SEQUENCE NNUM_SEQ
START WITH 1
INCREMENT BY 1
;

-------------------------------�������� ���� ����----------------------------------
INSERT INTO NOTICE VALUES(NNUM_SEQ.NEXTVAL, '��������', '�ȳ��ϼ��� ������� ���α׷��Դϴ�.', 'D0002', to_char(sysdate,'YYYY-MM-DD HH:MI:SS'));
INSERT INTO NOTICE VALUES(NNUM_SEQ.NEXTVAL, '�ʼ�����', '�ȳ��ϼ���', 'P9999', to_char(sysdate,'YYYY-MM-DD HH:MI:SS'));

-------------------------------�������� Ȯ��-------------------------------------
DESC NOTICE;
SELECT * FROM NOTICE;
DROP SEQUENCE NNUM_SEQ;
DELETE FROM NOTICE;

COMMIT;


