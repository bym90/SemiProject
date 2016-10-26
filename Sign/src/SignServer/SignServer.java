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
	// ���� ���� �غ�
	public ServerSocket server;
	// ������ Ŭ���̾�Ʈ ������ ���� �÷��� �غ�
	public ArrayList clientList;
	// ������ ���̽� ó���� �����غ�
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
			// ���� ���� �����
			server = new ServerSocket(4444);
			// Ŭ���̾�Ʈ ������ ����� �غ� �Ѵ�.
			clientList = new ArrayList();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Ŭ���̾�Ʈ�� ��û�� ���� �����ͺ��̽��� ����� �غ� �Ѵ�.
		try {
			db = new MyJDBC(); // ����̹� �ε�
			con = db.getCon(); // ���ӽõ�
			SignServerSql S = new SignServerSql();
			
			
			reserStateS = db.getPstmt(S.roomReserveState, con); //�ش� PreparedStatement �غ�.
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
			// ��������â�� ��������� ���� ����
			attendEmpS = db.getPstmt(S.attendEmp, con);
			// ���������� ���´��� ������ ��������
//			infoS = db.getPstmt(S.info, con);
			// ���´��� ������ �������� �߰�
			attendS = db.getPstmt(S.attend, con);
			attendManaS = db.getPstmt(S.attendMana, con);
			// �α�������
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
		System.out.println("//===������ ����Ǿ����ϴ�.====*");
		System.out.println("//=============================*");
		while (true) {
			try {
				Socket socket = server.accept();
				// ������ �Ǹ� �ش� Ŭ���̾�Ʈ�� ����� �����带 ����� �־���Ѵ�
				System.out.println("���ӵǾ����ϴ�.");
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