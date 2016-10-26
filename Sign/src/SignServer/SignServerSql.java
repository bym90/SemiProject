package SignServer;

public class SignServerSql {
	String Cname = "select * from company";
	
	String Cdept = "select distinct cdept from company";
	
	String name = "select name||empno as name from company where cdept = ?";
	
	String ip = "select IP from company where empno = ?";
	// 회의룸 예약 프레임이 처음으로 화면에 나올 때, 방전체의 예약 현황이 테이블에 나와야 함.=======안치현 ====//
	
	String roomReserveState = "select r.cnum reserNo, c.name name, r.rnum roomNo, "
			+ "i.people people, r.purpo purpo, r.reserdate day,r.stime stime,r.etime etime, "
			+ "r.confirmstate confirmstate from crr r inner join company c on(r.empno = c.empno) "
			+ "inner join cri i on(r.rnum = i.rnum) order by r.cnum desc";
	
	String roomMachinesNum = "select * from cri where rnum = ?";
	
	String roomReserveInsert = "INSERT INTO CRR VALUES(CNUM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?,'대기')";
	
	String crrLastRow = "select * from crr where cnum = (select max(CNUM)from crr)";
	
	String updateConfirmState = "update crr set confirmstate = '승인' where cnum = ?";
	
	String updateConfirmStateReject = "update crr set confirmstate = '반려' where cnum = ?";
	
	String roomDeleteRow = "delete from crr where cnum = ?";
	
	String loginQuery = "select * from company where empno=? and password=?"; 
	
	String findPassQuery = "select empno,name,password,email from company where empno=? and email=?";
	
	String attendEmp = "select * from company where empno=?";

	String attendMana = "Select * from ATTENDANCE n INNER JOIN COMPANY c on(c.empno=n.empno) where cdept = ? ORDER BY DAY DESC, TIME DESC";
	
	// 메인프레임 근태단추 누를시 근태정보
//	String info = "SELECT * FROM ATTENDANCE ORDER BY ANUM";
	
	// 근태단추 누를시 근태정보 추가
	String attend = "INSERT INTO ATTENDANCE (ANUM, EMPNO, TYPE) VALUES(ANUM_SEQ.NEXTVAL, ?, ?)";
	
	// 로그인정보
	String attend2 = "select * from attendance where empno=? ORDER BY ANUM DESC";
					
	String notice = "select NNUM, name, TITLE, WDATE from NOTICE n INNER JOIN COMPANY c on(c.empno = n.empno) ORDER BY NNUM DESC" ;
	
	String noticeSave = "insert into NOTICE (NNUM, EMPNO, TITLE, CONTENT) VALUES(NNUM_SEQ.NEXTVAL, ?, ?, ?)";
	
	String noticeModify = "select NNUM, name, CONTENT, TITLE, WDATE from NOTICE n INNER JOIN COMPANY c on(c.empno = n.empno) Where NNUM=? ORDER BY NNUM DESC";
	
	String noticeModifyButton = "update NOTICE set TITLE=?, CONTENT=? where NNUM=?";
		
	String noticeDelete = "delete from NOTICE where NNUM=?";
	
	String chatEmp = "select name||empno as name from company where empno = ?";

	
	
}
