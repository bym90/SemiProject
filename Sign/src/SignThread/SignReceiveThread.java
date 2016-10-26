package SignThread;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import NetData.*;
import SignClient.SignChat.*;
import SignClient.SignIn.SignToSendMail;
import SignClient.SignNotice.*;
import SignMainFrame.MainFrame;
import SignServer.SignServer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import SignClient.SignChat.*;
import SignClient.SignAttendance.*;
import SignClient.SignRoomReserve.*;
import SignClient.Tray.TrayIconApp;

public class SignReceiveThread extends Thread {
	MainFrame main;
	RoomReserve room;
	ChatCard chat;
	Object[][] ob;
	// SignNotice snMain;
	SignNoticeRead Snread;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public SignReceiveThread(MainFrame m) {
		main = m;
		main.chat = new ChatCard(main);
		main.chat.setSize(470, 600);
		main.chat.setLocation(((screenSize.width) / 2) - 235, ((screenSize.height) / 2) - 300);
	}

	public void roomPropertyProc(MainData data) {
		System.out.println("SignReceiveThread: entered PropertyProc.");
		main.RoomReserForm.projector.setText(data.roData.projector);
		main.RoomReserForm.mic.setText(data.roData.mic);
		main.RoomReserForm.PC.setText(data.roData.pc);
	}

	// 예약버튼을 누를시에, DB에 추가된 행을, 다시 table에 추가.
	public void tableaddrowProc(MainData data) {
		System.out.println("SignReceiveThread: entered tableaddrowProc.");
		Object[] obj = data.rData.reserveTest;
		main.RoomReserForm.addOneRow(obj);
	}

	// DB에서 받은 회의실 예약현황을 테이블에 뿌렸다.
	public void roomReserveProc(MainData data) {
		System.out.println("SignReceiveThread: entered roomReserveProc.");
		Object[][] temp = data.rData.ReserveNo;
		System.out.println(temp[0][1]);
		main.RoomReserForm.rowToTable(temp);
		main.RoomReserForm.setSize(800, 298);
		main.RoomReserForm.setLocation(((screenSize.width) / 2) - 400, ((screenSize.height) / 2) - 149);
		main.RoomReserForm.setVisible(true);
	}

	public void enterProc(MainData data) {
		// 클라이언트 스레드에서 받아온 MainData에 연결된 ChatData의 값을 넣어줌
		main.chat.Snamesize = data.cData.SnameSize;
		main.chat.Mnamesize = data.cData.MnameSize;
		main.chat.Pnamesize = data.cData.PnameSize;
		main.chat.Dnamesize = data.cData.DnameSize;
		main.chat.chatSname = data.cData.SNameList;
		main.chat.chatMname = data.cData.MNameList;
		main.chat.chatPname = data.cData.PNameList;
		main.chat.chatDname = data.cData.DNameList;
		main.chat.deptsize = data.cData.deptsize;
		main.chat.chatdept = data.cData.DeptList;

		// 정보를 다 받아오고 Jtree를 만든다.
		main.chat.renewProc();
		main.chat.setVisible(true);
	}

	public void showMsg(MainData data) {
		// 텍스트 에리어에 출력한다.
		main.chat.area.append(data.cData.msg + "\r\n");
		main.chat.area.setCaretPosition(main.chat.area.getDocument().getLength());
	}

	public void exitProc(MainData data) {
		main.chat.setVisible(false);
	}

	public void upProc(MainData data) {
		JOptionPane.showMessageDialog(main, "파일 업로드 성공");
	}

	public void listProc(MainData data) {
		main.dDlg = new DownloadDlg(main);
		// 응답온 결과를 목록에 출력한다.
		main.dDlg.list.setListData(data.fData.files);
		main.dDlg.setSize(350, 350);
		main.dDlg.setLocation(((screenSize.width) / 2) - 175, ((screenSize.height) / 2) - 175);
		main.dDlg.setVisible(true);
	}

	public void downProc(MainData data) {
		System.out.println("파일이름" + data.fData.fileName);
		File file = new File("D://Download", data.fData.fileName);
		// 스트림을 이용해서 파일을 저장한다
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(data.fData.buff);
			JOptionPane.showMessageDialog(main.dDlg, "다운로드 완료");
		} catch (Exception e) {
		} finally {
			try {
				fout.close();
			} catch (Exception e) {
			}
		}
	}

	public void setIP(MainData data) {
		main.chat.ip = data.mData.IP;
	}

	// 받은 db데이터 테이블에 뿌려주기........
	public void attendinfoProcS(MainData data) {

		String[] temp = { "날짜", "시간", "형태" };
		main.af.model.setDataVector(data.aData.attendAll, temp);

		main.af.setSize(600, 600);
		main.af.setLocation(((screenSize.width) / 2) - 300, ((screenSize.height) / 2) - 300);
		main.af.setVisible(true);
	}

	public void attendEmpProcS(MainData data) {
		main.af.v2.setText(data.name);
		main.af.v3.setText(data.empno);
		main.af.v4.setText(data.cdept);
		main.af.v5.setText(data.crank);
	}

	public void attendManagerS(MainData data) {
		String[] temp = { "날짜", "시간", "이름", "사원번호", "형태" };
		main.af.model.setDataVector(data.aData.attendWorkerAll, temp);

		main.af.setSize(600, 600);
		main.af.setVisible(true);
	}

	public void loginProc(MainData data) {
		System.out.println("로그인 성공여부:" + data.isSuccess);
		if (data.isSuccess == true) {
			InetAddress ipa = null;
			try {
				ipa = InetAddress.getLocalHost();
				main.ip = ipa.getHostAddress();
				main.domain = ipa.getHostName();
				System.out.println("접속IP주소:" + main.ip + "접속도메인:" + main.domain);
			} catch (Exception e) {
				System.out.println("IP받아오기 오류:" + e);
			}
			main.empno = data.empno;
			main.cdept = data.cdept;
			main.crank = data.crank;
			main.name = data.name;
			String birthday = data.birth;
			main.birth = birthday.substring(0, 10);
			// 로그인 온라인 여부를 세팅하자.
			main.loginState = "Y"; // 로그인 상태를 넣어준다.
			// 메인창을 다시 로그인하면 띄운다.
			main.setLocation(0, 0);
			main.setLocation((main.screenSize.width) - 320, 0);
			main.setVisible(true);

			// 로그인 창을 없앤다.
			main.log.setVisible(false);

			// main.tray.
			TrayIconApp tray = new TrayIconApp("Sign Ver1.0 사원 인트라넷", main);

			// 채팅방을 위한 부서번호 변수
			main.room = data.cdept;

			main.empnoL.setText("사원번호: " + data.empno);
			main.nameL.setText("이       름: " + data.name);
			main.crankL.setText("직       책: " + data.crank);
			main.cdeptL.setText("부       서: " + data.cdept);
			main.birthL.setText("생년월일: " + data.birth);
			main.ipL.setText("접 속 I  P : " + main.ip);
			main.domainL.setText("접속도메인 : " + main.domain);
			if (main.loginState.equals("Y")) {
				main.loginStateL.setText("접속여부: 온라인");
			} else {
				main.loginStateL.setText("접속여부: 오프라인");
			}

		} else {
			// 실패하면 메시지를 출력해줄 예정이다.
			JOptionPane.showMessageDialog(main, "로그인에 실패했습니다.");
		}
	}

	public void setAccessedEmp(MainData data) {
		// 현재 접속한 사원들을 출력한다.
		main.chat.NameEmpno = data.NameEmpno;
		main.chat.EmpsSize = data.cData.EmpsNum;
		main.chat.Emps = data.cData.Emps;
		// 누군가 접속하면 제이트리 갱신
		main.chat.renewProc();
		
		if(data.NameEmpno==null){
			return;
		}
		else if(main.empno.substring(0,1).equals(
			data.NameEmpno.substring(data.NameEmpno.length()-5,data.NameEmpno.length()-4)))
		{
		main.chat.area.append(data.NameEmpno +"님이 접속 하셨습니다."+"\r\n");
		}
	}

	public void passFindProc(MainData data) {
		System.out.println("비번찾기 성공여부:" + data.isSuccess);
		System.out.println("비밀번호 값 return:" + data.pw);
		if (data.isSuccess == true) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				// 암호화
				String decodePW = new String(decoder.decodeBuffer(data.pw));
				System.out.println("복호화 된 암호:" + decodePW);
				JOptionPane.showMessageDialog(null,
						new JLabel("<html>" + "<body><font color='blue'>" + data.name + "님 정보가 있습니다.</font><br>"
								+ "사원번호: " + data.empno + "<br>" + "가입하신 " + data.email + " 주소로 발송처리하였습니다.<br>"
								+ "</body></html>)"),
						"비밀번호 찾기", JOptionPane.OK_OPTION);
				try {
					// DB의 암호화된 비밀번호 복호화하여 이메일 전송
					SignToSendMail.recipient = data.email.trim();
					SignToSendMail.subject = "(주)Sign 비밀번호를 발급하였습니다.";
					SignToSendMail.body = data.name + "님(" + data.empno.toUpperCase() + ")의 비밀번호는 다음과 같습니다.\n"
							+ "비밀번호: " + decodePW + "\n" + "\n 본인이 요청한 정보가 아니면 관리자에게 문의하시기 바랍니다.\n감사합니다.\n"
							+ "=====================================";
					SignToSendMail.filename = "src/images/pwFind.gif";
					// =======테스트 출력
					System.out.println("SignToSendMail.recipient:-> " + SignToSendMail.recipient);
					System.out.println("SignToSendMail.subject-> " + SignToSendMail.subject);
					System.out.println("SignToSendMail.body" + SignToSendMail.body);
					new SignToSendMail();
				} catch (Exception e) {
					System.out.println("이메일 전송 오류:" + e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				System.out.println("복호화 오류임돵:" + e);
			}
		} else {
			// 실패하면 메시지를 출력해줄 예정이다.
			JOptionPane.showMessageDialog(main, "일치하는 정보가 없습니다.");
		}
	}

	public void noticeProcS(MainData data) {
		String[] temp = { "글번호", "제목", "작성자", "작성일" };
		main.sn.model.setDataVector(data.nData.noticeAll, temp);

		main.sn.table.getColumn("글번호").setMaxWidth(40);
		main.sn.table.getColumn("제목").setMaxWidth(400);
		main.sn.table.getColumn("작성자").setMaxWidth(80);
		main.sn.table.getColumn("작성일").setMaxWidth(140);

		main.sn.setSize(660, 600);
		main.sn.setVisible(true);
	}

	public void writeProcS(MainData data) {
		main.buttonInfo = data.nData.writeInfo;

		main.sn.Snw.nameL.setText("작성자 : " + data.name);
		main.sn.Snw.area.setText("");
		main.sn.Snw.field.setText("");
		main.sn.Snw.setSize(660, 300);
		main.sn.Snw.setVisible(true);
	}

	public void saveProcS(MainData data) {
		main.sn.Snw.field.setText("");
		main.sn.Snw.area.setText("");
		main.sn.Snw.dispose();
	}

	public void modifyProcS(MainData data) {
		main.buttonInfo = data.nData.modifyInfo;
		main.nnum = data.nData.nNum;

		main.sn.Snw.nameL.setText("작성자 : " + data.nData.name);
		main.sn.Snw.field.setText(data.nData.title);
		main.sn.Snw.area.setText(data.nData.content);

		main.sn.Snw.setSize(660, 300);
		main.sn.Snw.setVisible(true);

	}

	public void readProcS(MainData data) {
		main.sn.Snread.nameL.setText("작성자 : " + data.nData.name);
		main.sn.Snread.field.setText(data.nData.title);
		main.sn.Snread.area.setText(data.nData.content);

		main.sn.Snread.setSize(660, 300);
		main.sn.Snread.setVisible(true);
	}

	public void run() {

		try {
			while (true) {
				System.out.println("ReceiveThread : while st. running.");
				MainData data = (MainData) main.ois.readObject();
				if (data == null) {
					break;
				}
				switch (data.protocol) {

				case 4001:
					noticeProcS(data);
					saveProcS(data);
					break;

				case 4102:
					writeProcS(data);
					break;

				case 4302:
					modifyProcS(data);
					break;

				case 4502:
					readProcS(data);
					break;

				case 2102: // 2102 로그인 함수
					loginProc(data);
					break;

				// 채팅방 들어가기
				case 2001:
					enterProc(data);
					break;

				case 2401: // 일반 대화
					showMsg(data);
					break;

				case 2302: // 방퇴장
					exitProc(data);
					break;

				case 2501: // 업로드
					upProc(data);
					break;
				case 2601: // 목록
					listProc(data);
					break;
				case 2602: // 다운로드
					downProc(data);
					break;
				case 1702:

					setIP(data); // 쪽지보내기 위해 상대방 아이피주소를 얻어옴
					break;

				case 2100: // DB정보 테이블에 뿌려주기
					attendinfoProcS(data);
					break;
				case 2111:
					attendEmpProcS(data);
					break;
				case 2223:
					attendManagerS(data);
					break;
				// ===================치현=================//
				case 3001:
					roomReserveProc(data);
					break;
				case 3003:
					roomPropertyProc(data);
					break;
				case 3102:
					tableaddrowProc(data);
					break;
				// ===================치현=================//

				// 시작하자마자 사원 정보를 가져다가 JTree에 뿌려주기(쪽지보내기)
				case 0000:
					setAccessedEmp(data);
					break;
				case 2202: // 2202 비번찾기 함수
					passFindProc(data);
					break;
				}
			}
		} catch (Exception e) {		
		}
	}
}