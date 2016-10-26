package SignServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import SignClient.SignRoomReserve.RoomReserve;
import SignThread.SignClientThread;
import javautil.MyJDBC;

public class SignServer {
	// 대기용 소켓 준비
	public ServerSocket server;
	// 접속한 클라이언트 관리를 위한 컬렉션 준비
	public ArrayList clientList;
	// 데이터 베이스 처리를 위한준비
	public MyJDBC db;
	public Connection con;

	public PreparedStatement loginS, commitS, infoS, infoS2, attendS;
	public PreparedStatement reserStateS, roomdeviceS, roomreserInsertS;
	public PreparedStatement roomApproveS, roomRejectS, crrLastRowS, roomS, roomDeleteS, noticeS; 
	public PreparedStatement Chatdept, ChatSname, ChatMname, ChatPname,chatEmp;
	public PreparedStatement ChatDname, MessageIp;
	public PreparedStatement attendEmpS, attendS2, noticeSaveS;
	public PreparedStatement noticeModifyS, noticeModifyButtonS, noticeDeleteS, attendManaS;

	public PreparedStatement findpassS;
	public ResultSet rs;

	public SignServer() {
		
		try {
			// 서버 소켓 만들기
			server = new ServerSocket(4444);
			// 클라이언트 정보를 기억할 준비를 한다.
			clientList = new ArrayList();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// 클라이언트의 요청에 따라서 데이터베이스를 사용할 준비를 한다.
		try {
			db = new MyJDBC(); // 드라이버 로딩
			con = db.getCon(); // 접속시도
			SignServerSql S = new SignServerSql();
			
			
			reserStateS = db.getPstmt(S.roomReserveState, con); //해당 PreparedStatement 준비.
			roomdeviceS = db.getPstmt(S.roomMachinesNum, con);
			roomreserInsertS = db.getPstmt(S.roomReserveInsert, con);
			crrLastRowS = db.getPstmt(S.crrLastRow, con);
			roomApproveS = db.getPstmt(S.updateConfirmState, con);
			roomRejectS = db.getPstmt(S.updateConfirmStateReject, con);
			roomDeleteS = db.getPstmt(S.roomDeleteRow, con);
			chatEmp = db.getPstmt(S.chatEmp, con);
			Chatdept = db.getPstmt(S.Cdept, con);
			ChatSname = db.getPstmt(S.name, con);
			ChatMname = db.getPstmt(S.name, con);
			ChatPname = db.getPstmt(S.name, con);
			ChatDname = db.getPstmt(S.name, con);
			MessageIp = db.getPstmt(S.ip, con);
			loginS = db.getPstmt(S.loginQuery, con);
			findpassS = db.getPstmt(S.findPassQuery, con);
			// 근태정보창에 사원정보를 위한 쿼리
			attendEmpS = db.getPstmt(S.attendEmp, con);
			// 메인프레임 근태단추 누를시 근태정보
//			infoS = db.getPstmt(S.info, con);
			// 근태단추 누를시 근태정보 추가
			attendS = db.getPstmt(S.attend, con);
			attendManaS = db.getPstmt(S.attendMana, con);
			// 로그인정보
			attendS2 = db.getPstmt(S.attend2, con);
			noticeS = db.getPstmt(S.notice, con);
			noticeSaveS = db.getPstmt(S.noticeSave, con);
			noticeModifyS = db.getPstmt(S.noticeModify, con);
			noticeModifyButtonS = db.getPstmt(S.noticeModifyButton, con);
			noticeDeleteS = db.getPstmt(S.noticeDelete, con);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("//=============================*");
		System.out.println("//===서버가 실행되었습니다.====*");
		System.out.println("//=============================*");
		while (true) {
			try {
				Socket socket = server.accept();
				// 접속이 되면 해당 클라이언트와 통신할 스레드를 만들어 주어야한다
				System.out.println("접속되었습니다.");
				SignClientThread t = new SignClientThread(this, socket);
				t.start();
				
				clientList.add(t);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		new SignServer();

	}
}