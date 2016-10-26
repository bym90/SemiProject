package SignServer;

public class SignServerSql {
	String Cname = "select * from company";
	
	String Cdept = "select distinct cdept from company";
	
	String name = "select name||empno as name from company where cdept = ?";
	
	String ip = "select IP from company where empno = ?";
	// ȸ�Ƿ� ���� �������� ó������ ȭ�鿡 ���� ��, ����ü�� ���� ��Ȳ�� ���̺� ���;� ��.=======��ġ�� ====//
	
	String roomReserveState = "select r.cnum reserNo, c.name name, r.rnum roomNo, "
			+ "i.people people, r.purpo purpo, r.reserdate day,r.stime stime,r.etime etime, "
			+ "r.confirmstate confirmstate from crr r inner join company c on(r.empno = c.empno) "
			+ "inner join cri i on(r.rnum = i.rnum) order by r.cnum desc";
	
	String roomMachinesNum = "select * from cri where rnum = ?";
	
	String roomReserveInsert = "INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?,'���')";
	
	String crrLastRow = "select * from crr where cnum = (select max(CNUM)from crr)";
	
	String updateConfirmState = "update crr set confirmstate = '����' where cnum = ?";
	
	String updateConfirmStateReject = "update crr set confirmstate = '�ݷ�' where cnum = ?";
	
	String roomDeleteRow = "delete from crr where cnum = ?";
	
	String loginQuery = "select * from company where empno=? and password=?"; 
	
	String findPassQuery = "select empno,name,password,email from company where empno=? and email=?";
	
	String attendEmp = "select * from company where empno=?";

	String attendMana = "Select * from ATTENDANCE n INNER JOIN COMPANY c on(c.empno=n.empno) where cdept = ? ORDER BY DAY DESC, TIME DESC";
	
	// ���������� ���´��� ������ ��������
//	String info = "SELECT * FROM ATTENDANCE ORDER BY ANUM";
	
	// ���´��� ������ �������� �߰�
	String attend = "INSERT INTO ATTENDANCE (ANUM, EMPNO, TYPE) VALUES(ANUM_SEQ.NEXTVAL, ?, ?)";
	
	// �α�������
	String attend2 = "select * from attendance where empno=? ORDER BY ANUM DESC";
					
	String notice = "select NNUM, name, TITLE, WDATE from NOTICE n INNER JOIN COMPANY c on(c.empno = n.empno) ORDER BY NNUM DESC" ;
	
	String noticeSave = "insert into NOTICE (NNUM, EMPNO, TITLE, CONTENT) VALUES(NNUM_SEQ.NEXTVAL, ?, ?, ?)";
	
	String noticeModify = "select NNUM, name, CONTENT, TITLE, WDATE from NOTICE n INNER JOIN COMPANY c on(c.empno = n.empno) Where NNUM=? ORDER BY NNUM DESC";
	
	String noticeModifyButton = "update NOTICE set TITLE=?, CONTENT=? where NNUM=?";
		
	String noticeDelete = "delete from NOTICE where NNUM=?";
	
	String chatEmp = "select name||empno as name from company where empno = ?";

	
	
}
