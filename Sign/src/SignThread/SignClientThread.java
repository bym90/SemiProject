package SignThread;

import java.io.*;
import java.net.*;
import NetData.*;
import SignClient.SignAttendance.AttendanceForm;
import SignMainFrame.MainFrame;
import SignServer.SignServer;
import sun.applet.Main;
import sun.misc.BASE64Encoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class SignClientThread extends Thread {

	public SignServer Smain;
	public Socket socket;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;

	public String[] Snametemp;
	public String[] Mnametemp;
	public String[] Pnametemp;
	public String[] Dnametemp;

	public int noticeSize;
	public int Snamesize;
	public int Mnamesize;
	public int Pnamesize;
	public int Dnamesize;

	// 근태현황을 저장하기위한 변수
	public Object[][] allA;
	public Object[][] allWorkerA;

	// 공지사항현황을 저장하기위한 변수
	public Object[][] allN;
	// 수정작업을 위해 질의문을 통해 알아낸 임시변수
	public String tempT;
	public String tempC;
	public String tempN;
	// 수정버튼이 눌렸다는 정보를 저장하기 위한 변수
	public String modifyTemp;
	// 선택한 행 정보를 저장하기 위한 변수
	public Integer nNum;
	// 클릭수를 저장하기 위한 변수
	public Integer clickCount;
	public Object[][] roomTemp;
	// 각 컬럼의 사이즈
	public int empnoSize;
	public int typeSize;
	public int attendSize;
	public int roomDataSize;
	public int allWorkerSize;

	// 로그인
	public String empno;
	public String pw;
	// 사원정보
	public String name;
	public String gen;
	public String birth;
	public String tel;
	public String email;
	public String cdept;
	public String crank;
	public String hiredate;
	public String creatdate;
	String NameEmpno;
	String room;
	// 쪽지보내기 상대방 아이피
	String otherIP = "";
	int AccessedEmpsize;
	String[] AccessedEmps;
	SignProc SP = new SignProc();

	public SignClientThread(SignServer m, Socket s) throws Exception {
		Smain = m;
		socket = s;

		
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();

		oos = new ObjectOutputStream(os);
		ois = new ObjectInputStream(is);

		System.out.println("ClientThread Constructor: streams are made."); // 치현

	}

	// 회의실 예약: 승인, 반려, 삭제를 할 수 있는 method.
	public void confirmStateChangeProc(MainData data) {
		System.out.println("ClientThread: Entered confirmStateChangeProc.");
		MainData maindata = data;
		System.out.println(maindata.rData.whichButton);
		switch (maindata.rData.whichButton) {// 삭제버튼이 아닐 경우: 승인/반려 절차로 들어간다.
		case 1: // 승인
			try {
				Smain.roomApproveS.setString(1, Integer.toString(maindata.rData.cnumber));
				Smain.roomApproveS.execute();
				System.out.println("ClientThread: Reservation's confirmation state is updated. ");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RoomEnterProc(maindata);
			break;
		case 2: // 반려
			try {
				Smain.roomRejectS.setString(1, Integer.toString(maindata.rData.cnumber));
				Smain.roomRejectS.execute();
				System.out.println("ClientThread: Selected row deleted.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RoomEnterProc(maindata);
			break;
		case 3: // 삭제
			try {
				Smain.roomDeleteS.setString(1, Integer.toString(maindata.rData.cnumber));
				Smain.roomDeleteS.execute();
				System.out.println("ClientThread: Selected row deleted.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RoomEnterProc(maindata);
			break;
		}
	}

	// 회의실 예약: 데이터 베이스에 추가 후, 그것을 다시 테이블에 추가하는 method.
	public void RoomReserveProc(MainData data) {
		try {
			System.out.println("ClientThread: Entered RoomReserveProc()");
			MainData mainData = data; // 데이터 들어갔음.
			Smain.roomreserInsertS.setString(1, mainData.rData.empno);
			Smain.roomreserInsertS.setString(2, mainData.rData.roomNum);
			Smain.roomreserInsertS.setString(3, mainData.rData.purpose);
			Smain.roomreserInsertS.setString(4, mainData.rData.reserDay);
			Smain.roomreserInsertS.setString(5, mainData.rData.startingTime);
			Smain.roomreserInsertS.setString(6, mainData.rData.endingTime);
			Smain.roomreserInsertS.execute(); // 데이터베이스에 insert 명령실행!!
			Smain.rs = Smain.crrLastRowS.executeQuery(); // insert된 행을 select한
															// 결과문을 resultset에
															// 저장!
			
			RoomEnterProc(mainData); 
			Object[] temp = new Object[9];
			while (Smain.rs.next()) {
				temp[0] = Smain.rs.getInt("cnum");
				temp[1] = mainData.rData.name;
				temp[2] = Smain.rs.getString("rnum");
				temp[3] = mainData.rData.people;
				temp[4] = Smain.rs.getString("purpo");
				temp[5] = Smain.rs.getString("reserdate");
				temp[6] = Smain.rs.getString("stime");
				temp[7] = Smain.rs.getString("etime");
				temp[8] = "대기";
			}

			mainData.rData.reserveTest = temp;

			mainData.protocol = 3102;

			try {
				oos.writeObject(mainData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
	}

	// 회의실 예약: 콤보 상자 선택 시에, 해당 방의 보유 기기를 라벨에 표시.
	public void RoomPropertyProc(MainData data) {
		try {
			System.out.println("ClientThread, RoomPropertyProc(): Database query is saved in resultset(SignServer, rs)");
			MainData mainData = new MainData();
			RoomData roomdata = new RoomData();

			// projector, mic, pc 유무
			Smain.roomdeviceS.setString(1, data.roData.roomname);
			Smain.rs = Smain.roomdeviceS.executeQuery();
			roomDataSize = SP.getResultSetSize(Smain.rs);
			String[] temp = new String[3];
			while (Smain.rs.next()) {
				temp[0] = Smain.rs.getString("BEAMYN");
				temp[1] = Smain.rs.getString("MICYN");
				temp[2] = Smain.rs.getString("PCYN");
				System.out.println(temp[0]);//// 데이터 들어감.
				roomdata.projector = temp[0];
				roomdata.mic = temp[1];
				roomdata.pc = temp[2];
				mainData.roData = roomdata;
				mainData.protocol = 3003;
			}

			try {
				oos.writeObject(mainData);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
	}

	// 회의실 예약: 최초, DB로부터, 회의실 예약 현황 정보를 불러와서 table에 넣는다.
	public void RoomEnterProc(MainData data) {
		try {
			System.out.println("ClientThread: entered RoomEnterProc().");
			Smain.rs = Smain.reserStateS.executeQuery(); // DataBean의
															// ResultSet에,
															// query문의 결과 저장.
			System.out.println("ClientThread, RoomEnterProc(): Database query is saved in resultset(SignServer, rs)");
			roomDataSize = SP.getResultSetSize(Smain.rs); // 해당 ResultSet의 size를
															// 따로 구함.
			System.out.println("ClientThread, RoomEnterProc(): resultset rows: " + roomDataSize);
			roomTemp = new Object[roomDataSize][9];
			int i = 0;
			while (Smain.rs.next()) {
				roomTemp[i][0] = Smain.rs.getInt("reserNo");
				roomTemp[i][1] = Smain.rs.getString("name");
				roomTemp[i][2] = Smain.rs.getString("roomNo");
				roomTemp[i][3] = Smain.rs.getInt("people");
				roomTemp[i][4] = Smain.rs.getString("purpo");
				roomTemp[i][5] = Smain.rs.getString("day");
				roomTemp[i][6] = Smain.rs.getString("stime");
				roomTemp[i][7] = Smain.rs.getString("etime");
				roomTemp[i][8] = Smain.rs.getString("confirmstate");
				i++;
			}
		} catch (Exception e) {
			System.out.println("Error : " + e);
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
		MainData mainData = new MainData();
		ReserveData reserData = new ReserveData();
		reserData.ReserveNo = roomTemp;
		mainData.rData = reserData;
		mainData.protocol = 3001;
		try {
			oos.writeObject(mainData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ClientThread: Requested data is saved into Databean.");
	}

	// 채팅 & 쪽지방 접속
	public void ChatEnterProc(MainData data) {
		// 자신이 소속된 부서끼리만 채팅 가능하도록 한다.
		room = data.cData.room;

		try {

			String Sname = "영업부";
			String Mname = "마케팅부";
			String Hname = "기획부";
			String Dname = "개발부";

			Smain.ChatSname.setString(1, Sname);
			Smain.rs = Smain.ChatSname.executeQuery();
			Snamesize = SP.getResultSetSize(Smain.rs);
			Snametemp = new String[Snamesize];
			int count = 0;
			while (Smain.rs.next()) {
				// 사원 이름 출력
				Snametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatMname.setString(1, Mname);
			Smain.rs = Smain.ChatMname.executeQuery();
			Mnamesize = SP.getResultSetSize(Smain.rs);
			Mnametemp = new String[Mnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// 사원 이름 출력
				Mnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatPname.setString(1, Hname);
			Smain.rs = Smain.ChatPname.executeQuery();
			Pnamesize = SP.getResultSetSize(Smain.rs);
			Pnametemp = new String[Pnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// 사원 이름 출력
				Pnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatDname.setString(1, Dname);
			Smain.rs = Smain.ChatDname.executeQuery();
			Dnamesize = SP.getResultSetSize(Smain.rs);
			Dnametemp = new String[Dnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// 사원 이름 출력
				Dnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}
		}

		catch (Exception e) {
			System.out.println("empThread 에러 = " + e);
			System.exit(0);
		} finally {
			Smain.db.close(Smain.rs);
		}
		// 지금 접속한 사원의 아이디(사원번호)를 받는다.
		empno = data.empno;

		MainData rData = new MainData();
		ChatData temp2 = new ChatData();

		temp2.SNameList = Snametemp;
		temp2.MNameList = Mnametemp;
		temp2.PNameList = Pnametemp;
		temp2.DNameList = Dnametemp;
		temp2.SnameSize = Snamesize;
		temp2.MnameSize = Mnamesize;
		temp2.PnameSize = Pnamesize;
		temp2.DnameSize = Dnamesize;
		rData.cData = temp2;
		boolean isSuccess = true;

		rData.protocol = 2001;
		rData.isSuccess = isSuccess;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	// 채팅내용 전송
	public void sendMsg(MainData data) {

		String msg = "[" + name + "]님  : " + data.cData.msg;

		MainData rData = new MainData();
		rData.protocol = 2401;
		ChatData temp = new ChatData();
		temp.msg = msg;
		rData.cData = temp;

		synchronized (Smain.clientList) {
			int size = Smain.clientList.size();
			for (int i = 0; i < size; i++) {
				// 지금 접속한 사원들을 알아본다.
				SignClientThread t = (SignClientThread) Smain.clientList.get(i);
				// 같은 부서사원에게만 메시지를 전송하도록 한다.
				if (this.room.equals(t.room)) {
					try {
						t.oos.writeObject(rData);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	// 채팅방 나가기
	public void exitProc(MainData data) {

		MainData rData = new MainData();
		rData.protocol = 2302;
		rData.isSuccess = true;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	// 파일 업로드
	public void upProc(MainData data) {
		// 파일의 위치를 잡는다
		File file = new File("D:\\Upload", data.fData.fileName);
		// 데이터를 스트림을 이용해서 저장한다.
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(data.fData.buff);
		} catch (Exception e) {
		} finally {
			try {
				fout.close();
			} catch (Exception e) {
			}
		}

		// 잘 받았다고 응답한다.
		MainData rData = new MainData();
		rData.protocol = 2501;
		rData.isSuccess = true;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void listProc(MainData data) {
		// 할일
		// 나는 D:\\Upload 폴더에 있는 파일의 이름을 몽땅 클라이언트에게 전송하고 싶다.
		File file = new File("D:\\Upload");
		String[] list = file.list();

		// 클라이언트에게 준다.
		MainData rData = new MainData();
		rData.protocol = 2601;
		FileData temp = new FileData();
		temp.files = list;
		rData.fData = temp;

		// 보낸다.
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void downProc(MainData data) {
		// 할일
		// 업로드와 반대로 서버에서는 파일의 내용을 읽어서

		File file = new File("D:\\Upload", data.fData.fileName);
		long size = file.length();
		byte[] buff = new byte[(int) size];
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			fin.read(buff);
		} catch (Exception e) {
		} finally {
			try {
				fin.close();
			} catch (Exception e) {
			}
		}

		// 그 내용을 클라이언트에게 보낸다.
		MainData rData = new MainData();
		rData.protocol = 2602;
		FileData temp = new FileData();
		temp.fileName = data.fData.fileName;
		temp.buff = buff;
		rData.fData = temp;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void getIP(MainData data) {

		try {
			Smain.MessageIp.setString(1, data.mData.Empno);
			Smain.rs = Smain.MessageIp.executeQuery();
			if (Smain.rs.next()) {
				otherIP = Smain.rs.getString("IP");
				System.out.println("상대방 아이피 : " + otherIP);
			} else {
				return;
			}

		} catch (Exception e) {

		}

		MainData rData = new MainData();
		rData.protocol = 1702;
		MessageData temp = new MessageData();
		temp.IP = otherIP;
		rData.mData = temp;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {

		}
	}

	// 최초 로그인시 쪽지목록을 가져온다.
	public void getAccessedEmp(MainData data) {
		// 접속한 사원의 아이디를 받는다.
		empno = data.empno;
		int size = Smain.clientList.size();
		// 접속한 사원의 아이디를 저장한다.
		String[] Emps = new String[size];

		MainData rData = new MainData();
		ChatData temp = new ChatData();
	
		//접속한 인원을 채팅창에 알려주기위한 정보
		try {
			Smain.chatEmp.setString(1, data.empno);
			
			Smain.rs = Smain.chatEmp.executeQuery();
			if(Smain.rs.next()){
				rData.NameEmpno = Smain.rs.getString("name");
				NameEmpno = Smain.rs.getString("name");
			}
			else{
				return;
			}
		} catch (SQLException e1) {}
		
		
		
		// 누가 접속을 했는지 알기 위해서 접속한 사원들의 아이디를 보내준다.
		rData.protocol = 0000;
		temp.EmpsNum = size;
		
			
		
		rData.cData = temp;

		for (int i = 0; i < size; i++) {
			// 현재 접속자 목록을 접속중인 사원들에게 전부 뿌림
			SignClientThread t = (SignClientThread) Smain.clientList.get(i);
			Emps[i] = t.empno;

		}

		temp.Emps = Emps;

		for (int i = 0; i < size; i++) {

			SignClientThread t = (SignClientThread) Smain.clientList.get(i);

			try {
				t.oos.writeObject(rData);
			} catch (Exception e) {

			}
		}
	}

	// 서버에서 나가면 채팅목록에서 지워지게 한다.
	public void getAccessedEmp() {
		AccessedEmpsize = Smain.clientList.size();
		// 접속한 사원의 아이디를 저장한다.
		AccessedEmps = new String[AccessedEmpsize];
		MainData rData = new MainData();
		ChatData temp = new ChatData();
		// 누가 접속을 했는지 알기 위해서 접속한 사원들의 아이디를 보내준다.
		rData.protocol = 0000;
		temp.EmpsNum = AccessedEmpsize;
		rData.cData = temp;
		for (int i = 0; i < AccessedEmpsize; i++) {
			// 현재 접속자 목록을 접속중인 사원들에게 전부 표시한다.
			SignClientThread t = (SignClientThread) Smain.clientList.get(i);
			AccessedEmps[i] = t.empno;
		}
		temp.Emps = AccessedEmps;
		for (int i = 0; i < AccessedEmpsize; i++) {
			SignClientThread t = (SignClientThread) Smain.clientList.get(i);
			try {
				t.oos.writeObject(rData);
			} catch (Exception e) {
			}
		}
	}

	// ============================근태관리 떴을때 db서버에서 데이터 받아오기=========
	public void attendinfoProcC(MainData data) {
		try {
			System.out.println("쿼리실행전");
			Smain.attendS2.setString(1, data.empno);
			Smain.rs = Smain.attendS2.executeQuery();
			System.out.println("쿼리실행후");
			attendSize = SP.getResultSetSize(Smain.rs);
			System.out.println(attendSize);
			allA = new Object[attendSize][3];
			int i = 0;
			while (Smain.rs.next()) {
				allA[i][0] = Smain.rs.getDate("DAY");
				allA[i][1] = Smain.rs.getString("TIME");
				allA[i][2] = Smain.rs.getString("TYPE");
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}

		MainData rData = new MainData();
		rData.protocol = 2100;
		AttendData temp = new AttendData();
		temp.attendAll = allA;
		rData.aData = temp;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attendEmpProcC(MainData data) {
		try {
			Smain.attendEmpS.setString(1, data.empno);
			System.out.println("근태 확인" + data.empno + "1");
			Smain.rs = Smain.attendEmpS.executeQuery();
			System.out.println("근태 확인" + data.empno + "2");
			Smain.rs.beforeFirst();
			System.out.println("근태 확인" + data.empno + "3");
			while (Smain.rs.next()) {
				System.out.println("근태 확인" + data.empno + "4");
				empno = Smain.rs.getString("EMPNO");
				name = Smain.rs.getString("NAME");
				cdept = Smain.rs.getString("CDEPT");
				crank = Smain.rs.getString("CRANK");
				System.out.println("근태정보 출력 empno :" + empno + "\t" + name + "\t" + cdept + "\t" + crank);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}

		MainData rData = new MainData();
		rData.empno = empno;
		rData.name = name;
		rData.cdept = cdept;
		rData.crank = crank;
		rData.protocol = 2111;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC1(MainData data) {
		try {
			System.out.println("근태확인 사원번호: " + data.empno);
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "출근");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC2(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "외근");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC3(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "조퇴");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC4(MainData data) {
		try {

			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "야근");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC5(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "퇴근");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void loginProc(MainData data) {
		boolean isSuccess = false;
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			String encodePassword = encoder.encode(data.pw.getBytes());
			System.out.println("SignClientThread 로그인 진입함수");
			Smain.loginS.setString(1, data.empno);
			System.out.println(":::::::::" + data.empno);
			Smain.loginS.setString(2, encodePassword);
			Smain.rs = Smain.loginS.executeQuery();

			if (Smain.rs.next()) {
				System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwww");
				empno = Smain.rs.getString("empno");
				name = Smain.rs.getString("name");
				pw = Smain.rs.getString("password");
				gen = Smain.rs.getString("gen");
				birth = Smain.rs.getString("birth");
				tel = Smain.rs.getString("tel");
				email = Smain.rs.getString("email");
				cdept = Smain.rs.getString("cdept");
				crank = Smain.rs.getString("crank");
				System.out.println("empno: " + empno + "  " + name + "  " + pw + "  " + "  " + gen + "  " + birth + "  "
						+ tel + "  " + email + "  " + cdept + "  " + crank);
				System.out.println("creatdate: " + creatdate);
				isSuccess = true;
				// 로그인에 성공했으므로 이 클라이언트의 정보 중에서
				// 이 사람의 이름을 기억한다.
			} else {
				isSuccess = false;
			}
		} catch (Exception e) {
		} finally {
			Smain.db.close(Smain.rs);
		}
		// 응답한다.
		// 응답 역시 클래스로 만들어서 응답을 하도록 한다.
		MainData rData = new MainData();
		rData.empno = empno;
		rData.name = name;
		rData.pw = pw;
		rData.gen = gen;
		rData.birth = birth;
		rData.tel = tel;
		rData.email = email;
		rData.cdept = cdept;
		rData.crank = crank;
		rData.protocol = 2102;
		rData.isSuccess = isSuccess;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void passFindProc(MainData data) {
		boolean isSuccess = false;
		try {
			System.out.println("passFindProc()진입1" + data.empno + "  " + data.email);
			Smain.findpassS.setString(1, data.empno);
			Smain.findpassS.setString(2, data.email);
			Smain.rs = Smain.findpassS.executeQuery();
			System.out.println("passFindProc()진입2");
			if (Smain.rs.next()) {
				empno = Smain.rs.getString("empno");
				name = Smain.rs.getString("name");
				pw = Smain.rs.getString("password");
				email = Smain.rs.getString("email");
				System.out.println("passFindProc()진입3");
				System.out.println("empno: " + empno);
				System.out.println("name: " + name);
				System.out.println("password: " + pw);
				System.out.println("email: " + email);
				System.out.println("passFindProc()진입4");
				isSuccess = true;
				// 비밀번호를 찾았다..
			} else {
				isSuccess = false;
			}
		} catch (Exception e) {
		} finally {
			Smain.db.close(Smain.rs);
		}
		// 응답한다.
		// 응답 역시 클래스로 만들어서 응답을 하도록 한다.
		MainData rData = new MainData();
		rData.protocol = 2202;
		rData.isSuccess = isSuccess;
		rData.empno = empno;
		rData.name = name;
		rData.email = email;
		rData.pw = pw;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void attendManagerC(MainData data) {
		try {
			System.out.println("입장한 사람 부서" + data.cdept);
			Smain.attendManaS.setString(1, data.cdept);
			Smain.rs = Smain.attendManaS.executeQuery();
			allWorkerSize = SP.getResultSetSize(Smain.rs);
			System.out.println("테이블 사이즈" + allWorkerSize);
			allWorkerA = new Object[allWorkerSize][5];
			int i = 0;
			while (Smain.rs.next()) {
				allWorkerA[i][0] = Smain.rs.getDate("DAY");
				allWorkerA[i][1] = Smain.rs.getString("TIME");
				allWorkerA[i][2] = Smain.rs.getString("NAME");
				allWorkerA[i][3] = Smain.rs.getString("EMPNO");
				allWorkerA[i][4] = Smain.rs.getString("TYPE");
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
		MainData rData = new MainData();
		rData.protocol = 2223;
		AttendData temp = new AttendData();
		temp.attendWorkerAll = allWorkerA;
		rData.aData = temp;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void noticeProcC(MainData data) {
		try {
			Smain.rs = Smain.noticeS.executeQuery();
			System.out.println("---------공지사항 질의 실행");
			noticeSize = SP.getResultSetSize(Smain.rs);
			System.out.println("-------행의 수" + noticeSize);
			allN = new Object[noticeSize][4];
			int i = 0;
			while (Smain.rs.next()) {
				allN[i][0] = Smain.rs.getInt("NNUM");
				allN[i][1] = Smain.rs.getString("TITLE");
				allN[i][2] = Smain.rs.getString("NAME");
				allN[i][3] = Smain.rs.getString("WDATE");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}

		MainData rData = new MainData();
		rData.protocol = 4001;
		NoticeData temp = new NoticeData();
		temp.noticeAll = allN;
		rData.nData = temp;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeProcC(MainData data) {
		MainData rData = new MainData();
		rData.protocol = 4102;
		rData.name = data.name;
		NoticeData temp = new NoticeData();
		temp.writeInfo = data.nData.writeInfo;
		rData.nData = temp;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveProcC(MainData data) {
		try {
			Smain.noticeSaveS.setString(1, data.empno);
			Smain.noticeSaveS.setString(2, data.nData.title);
			Smain.noticeSaveS.setString(3, data.nData.content);
			Smain.noticeSaveS.execute();
			System.out.println("내용과 제목 데이터베이스에 넣은 후");
			noticeProcC(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void modifyProcC(MainData data) {
		try {
			Smain.noticeModifyS.setInt(1, data.nData.nNum);
			Smain.rs = Smain.noticeModifyS.executeQuery();
			while (Smain.rs.next()) {
				tempT = Smain.rs.getString("TITLE");
				tempC = Smain.rs.getString("CONTENT");
				tempN = Smain.rs.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
		modifyTemp = data.nData.modifyInfo;
		nNum = data.nData.nNum;
		MainData rData = new MainData();
		rData.protocol = 4302;
		System.out.println("-------프로토콜" + rData.protocol);
		NoticeData temp = new NoticeData();
		System.out.println("--------제목" + tempT);
		System.out.println("--------내용" + tempC);
		System.out.println("--------이름" + tempN);
		temp.title = tempT;
		temp.content = tempC;
		temp.name = tempN;
		temp.modifyInfo = modifyTemp;
		temp.nNum = nNum;
		rData.nData = temp;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void modifyButtonProcC(MainData data) {
		try {
			Smain.noticeModifyButtonS.setString(1, data.nData.title);
			Smain.noticeModifyButtonS.setString(2, data.nData.content);

			Smain.noticeModifyButtonS.setInt(3, data.nData.nNum);

			Smain.noticeModifyButtonS.execute();

			noticeProcC(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readProcC(MainData data) {
		try {
			Smain.noticeModifyS.setInt(1, data.nData.nNum);
			Smain.rs = Smain.noticeModifyS.executeQuery();
			while (Smain.rs.next()) {
				tempT = Smain.rs.getString("TITLE");
				tempC = Smain.rs.getString("CONTENT");
				tempN = Smain.rs.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Smain.db.close(Smain.rs);
		}
		nNum = data.nData.nNum;
		MainData rData = new MainData();
		rData.protocol = 4502;
		NoticeData temp = new NoticeData();

		temp.title = tempT;
		temp.content = tempC;
		temp.name = tempN;
		temp.nNum = nNum;
		rData.nData = temp;
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteProcC(MainData data) {
		try {
			Smain.noticeDeleteS.setInt(1, data.nData.nNum);
			Smain.noticeDeleteS.execute();

			noticeProcC(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {

		try {
			while (true) {
				MainData data = (MainData) ois.readObject();
				if (data == null) {
					break;
				}

				switch (data.protocol) {
				case 1001:
					ChatEnterProc(data);
					break;
				case 1401: // 대화요청
					sendMsg(data);
					break;
				case 1302: // 방 퇴장
					exitProc(data);
					break;
				case 1501: // 업로드
					upProc(data);
					break;
				case 1601: // 파일 목록
					listProc(data);
					break;
				case 1602: // 다운로드
					downProc(data);
					break;
				case 1701: // 메시지를 보내기 위한 상대방 ip얻기
					getIP(data);
					break;
				case 9999: // 로그아웃할때 자신을 쪽지목록에서 제거
					Smain.clientList.remove(this);
					getAccessedEmp();
					break;
				case 2222:
					attendManagerC(data);
					attendEmpProcC(data);
					break;

				case 4000:
					noticeProcC(data);
					break;

				case 4101:
					System.out.println("-------글쓰기창 함수 실행전");
					writeProcC(data);
					break;

				case 4201:
					saveProcC(data);
					break;

				case 4301:
					modifyProcC(data);
					break;

				case 4401:
					modifyButtonProcC(data);
					break;

				case 4501:
					readProcC(data);
					break;

				case 4601:
					deleteProcC(data);
					break;
				case 1101:
					loginProc(data); // 1101 : 로그인처리
					getAccessedEmp(data); // 규영 (쪽지보내기 사원목록)
					break;
				case 1102:
					passFindProc(data); // 1102 : 비번찾기
					break;
				case 2000: // 근태현황 정보요청
					attendinfoProcC(data);
					attendEmpProcC(data);
					break;
				case 2101: // 출근시간 정보삽입과 동시에 근태현황 정보요청
					attendInProcC1(data);
					break;
				case 2102: // 외근시간 정보삽입과 동시에 근태현황 정보요청
					attendInProcC2(data);
					break;
				case 2103: // 조퇴시간 정보삽입과 동시에 근태현황 정보요청
					attendInProcC3(data);
					break;
				case 2104: // 야근시간 정보삽입과 동시에 근태현황 정보요청
					attendInProcC4(data);
					break;
				case 2105: // 퇴근시간 정보삽입과 동시에 근태현황 정보요청
					attendInProcC5(data);
					break;
				case 3000: // 회의실 예약: 최초, DB로부터, 회의실 예약 현황 정보를 불러와서 table에 넣는다.
					System.out.println(data);
					System.out.println("ClientThread: Protocol 3000 in.");
					RoomEnterProc(data);
					System.out.println("ClientThread: Protocol 3000 out.");
					break;
				case 3002: // 회의실 예약: 콤보 상자 선택 시에, 해당 방의 보유 기기를 라벨에 표시.
					System.out.println("ClientThread: Protocol 3002 in.");
					RoomPropertyProc(data);
					System.out.println("ClientThread: Protocol 3002 out.");
					break;
				case 3101:
					System.out.println("ClientThread: Protocol 3101 in.");
					RoomReserveProc(data);
					System.out.println("ClientThread: Protocol 3101 out.");
					break;
				case 3103:
					System.out.println("ClientThread: Protocal 3103 in");
					confirmStateChangeProc(data);
					System.out.println("ClientThread: Protocal 3103 out");
					break;
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (Exception e) {
			}
			Smain.clientList.remove(this);
			// 접속한 회원이 나가면 쪽지 목록에서 제거 시켜야함
			getAccessedEmp();

		}
	}
}
