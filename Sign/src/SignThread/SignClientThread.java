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

	// ������Ȳ�� �����ϱ����� ����
	public Object[][] allA;
	public Object[][] allWorkerA;

	// ����������Ȳ�� �����ϱ����� ����
	public Object[][] allN;
	// �����۾��� ���� ���ǹ��� ���� �˾Ƴ� �ӽú���
	public String tempT;
	public String tempC;
	public String tempN;
	// ������ư�� ���ȴٴ� ������ �����ϱ� ���� ����
	public String modifyTemp;
	// ������ �� ������ �����ϱ� ���� ����
	public Integer nNum;
	// Ŭ������ �����ϱ� ���� ����
	public Integer clickCount;
	public Object[][] roomTemp;
	// �� �÷��� ������
	public int empnoSize;
	public int typeSize;
	public int attendSize;
	public int roomDataSize;
	public int allWorkerSize;

	// �α���
	public String empno;
	public String pw;
	// �������
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
	// ���������� ���� ������
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

		System.out.println("ClientThread Constructor: streams are made."); // ġ��

	}

	// ȸ�ǽ� ����: ����, �ݷ�, ������ �� �� �ִ� method.
	public void confirmStateChangeProc(MainData data) {
		System.out.println("ClientThread: Entered confirmStateChangeProc.");
		MainData maindata = data;
		System.out.println(maindata.rData.whichButton);
		switch (maindata.rData.whichButton) {// ������ư�� �ƴ� ���: ����/�ݷ� ������ ����.
		case 1: // ����
			try {
				Smain.roomApproveS.setString(1, Integer.toString(maindata.rData.cnumber));
				Smain.roomApproveS.execute();
				System.out.println("ClientThread: Reservation's confirmation state is updated. ");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RoomEnterProc(maindata);
			break;
		case 2: // �ݷ�
			try {
				Smain.roomRejectS.setString(1, Integer.toString(maindata.rData.cnumber));
				Smain.roomRejectS.execute();
				System.out.println("ClientThread: Selected row deleted.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			RoomEnterProc(maindata);
			break;
		case 3: // ����
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

	// ȸ�ǽ� ����: ������ ���̽��� �߰� ��, �װ��� �ٽ� ���̺� �߰��ϴ� method.
	public void RoomReserveProc(MainData data) {
		try {
			System.out.println("ClientThread: Entered RoomReserveProc()");
			MainData mainData = data; // ������ ����.
			Smain.roomreserInsertS.setString(1, mainData.rData.empno);
			Smain.roomreserInsertS.setString(2, mainData.rData.roomNum);
			Smain.roomreserInsertS.setString(3, mainData.rData.purpose);
			Smain.roomreserInsertS.setString(4, mainData.rData.reserDay);
			Smain.roomreserInsertS.setString(5, mainData.rData.startingTime);
			Smain.roomreserInsertS.setString(6, mainData.rData.endingTime);
			Smain.roomreserInsertS.execute(); // �����ͺ��̽��� insert ��ɽ���!!
			Smain.rs = Smain.crrLastRowS.executeQuery(); // insert�� ���� select��
															// ������� resultset��
															// ����!
			
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
				temp[8] = "���";
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

	// ȸ�ǽ� ����: �޺� ���� ���� �ÿ�, �ش� ���� ���� ��⸦ �󺧿� ǥ��.
	public void RoomPropertyProc(MainData data) {
		try {
			System.out.println("ClientThread, RoomPropertyProc(): Database query is saved in resultset(SignServer, rs)");
			MainData mainData = new MainData();
			RoomData roomdata = new RoomData();

			// projector, mic, pc ����
			Smain.roomdeviceS.setString(1, data.roData.roomname);
			Smain.rs = Smain.roomdeviceS.executeQuery();
			roomDataSize = SP.getResultSetSize(Smain.rs);
			String[] temp = new String[3];
			while (Smain.rs.next()) {
				temp[0] = Smain.rs.getString("BEAMYN");
				temp[1] = Smain.rs.getString("MICYN");
				temp[2] = Smain.rs.getString("PCYN");
				System.out.println(temp[0]);//// ������ ��.
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

	// ȸ�ǽ� ����: ����, DB�κ���, ȸ�ǽ� ���� ��Ȳ ������ �ҷ��ͼ� table�� �ִ´�.
	public void RoomEnterProc(MainData data) {
		try {
			System.out.println("ClientThread: entered RoomEnterProc().");
			Smain.rs = Smain.reserStateS.executeQuery(); // DataBean��
															// ResultSet��,
															// query���� ��� ����.
			System.out.println("ClientThread, RoomEnterProc(): Database query is saved in resultset(SignServer, rs)");
			roomDataSize = SP.getResultSetSize(Smain.rs); // �ش� ResultSet�� size��
															// ���� ����.
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

	// ä�� & ������ ����
	public void ChatEnterProc(MainData data) {
		// �ڽ��� �Ҽӵ� �μ������� ä�� �����ϵ��� �Ѵ�.
		room = data.cData.room;

		try {

			String Sname = "������";
			String Mname = "�����ú�";
			String Hname = "��ȹ��";
			String Dname = "���ߺ�";

			Smain.ChatSname.setString(1, Sname);
			Smain.rs = Smain.ChatSname.executeQuery();
			Snamesize = SP.getResultSetSize(Smain.rs);
			Snametemp = new String[Snamesize];
			int count = 0;
			while (Smain.rs.next()) {
				// ��� �̸� ���
				Snametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatMname.setString(1, Mname);
			Smain.rs = Smain.ChatMname.executeQuery();
			Mnamesize = SP.getResultSetSize(Smain.rs);
			Mnametemp = new String[Mnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// ��� �̸� ���
				Mnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatPname.setString(1, Hname);
			Smain.rs = Smain.ChatPname.executeQuery();
			Pnamesize = SP.getResultSetSize(Smain.rs);
			Pnametemp = new String[Pnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// ��� �̸� ���
				Pnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}

			Smain.ChatDname.setString(1, Dname);
			Smain.rs = Smain.ChatDname.executeQuery();
			Dnamesize = SP.getResultSetSize(Smain.rs);
			Dnametemp = new String[Dnamesize];
			count = 0;
			while (Smain.rs.next()) {
				// ��� �̸� ���
				Dnametemp[count] = Smain.rs.getString("NAME");
				count++;
			}
		}

		catch (Exception e) {
			System.out.println("empThread ���� = " + e);
			System.exit(0);
		} finally {
			Smain.db.close(Smain.rs);
		}
		// ���� ������ ����� ���̵�(�����ȣ)�� �޴´�.
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

	// ä�ó��� ����
	public void sendMsg(MainData data) {

		String msg = "[" + name + "]��  : " + data.cData.msg;

		MainData rData = new MainData();
		rData.protocol = 2401;
		ChatData temp = new ChatData();
		temp.msg = msg;
		rData.cData = temp;

		synchronized (Smain.clientList) {
			int size = Smain.clientList.size();
			for (int i = 0; i < size; i++) {
				// ���� ������ ������� �˾ƺ���.
				SignClientThread t = (SignClientThread) Smain.clientList.get(i);
				// ���� �μ�������Ը� �޽����� �����ϵ��� �Ѵ�.
				if (this.room.equals(t.room)) {
					try {
						t.oos.writeObject(rData);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	// ä�ù� ������
	public void exitProc(MainData data) {

		MainData rData = new MainData();
		rData.protocol = 2302;
		rData.isSuccess = true;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	// ���� ���ε�
	public void upProc(MainData data) {
		// ������ ��ġ�� ��´�
		File file = new File("D:\\Upload", data.fData.fileName);
		// �����͸� ��Ʈ���� �̿��ؼ� �����Ѵ�.
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

		// �� �޾Ҵٰ� �����Ѵ�.
		MainData rData = new MainData();
		rData.protocol = 2501;
		rData.isSuccess = true;

		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void listProc(MainData data) {
		// ����
		// ���� D:\\Upload ������ �ִ� ������ �̸��� ���� Ŭ���̾�Ʈ���� �����ϰ� �ʹ�.
		File file = new File("D:\\Upload");
		String[] list = file.list();

		// Ŭ���̾�Ʈ���� �ش�.
		MainData rData = new MainData();
		rData.protocol = 2601;
		FileData temp = new FileData();
		temp.files = list;
		rData.fData = temp;

		// ������.
		try {
			oos.writeObject(rData);
		} catch (Exception e) {
		}
	}

	public void downProc(MainData data) {
		// ����
		// ���ε�� �ݴ�� ���������� ������ ������ �о

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

		// �� ������ Ŭ���̾�Ʈ���� ������.
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
				System.out.println("���� ������ : " + otherIP);
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

	// ���� �α��ν� ��������� �����´�.
	public void getAccessedEmp(MainData data) {
		// ������ ����� ���̵� �޴´�.
		empno = data.empno;
		int size = Smain.clientList.size();
		// ������ ����� ���̵� �����Ѵ�.
		String[] Emps = new String[size];

		MainData rData = new MainData();
		ChatData temp = new ChatData();
	
		//������ �ο��� ä��â�� �˷��ֱ����� ����
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
		
		
		
		// ���� ������ �ߴ��� �˱� ���ؼ� ������ ������� ���̵� �����ش�.
		rData.protocol = 0000;
		temp.EmpsNum = size;
		
			
		
		rData.cData = temp;

		for (int i = 0; i < size; i++) {
			// ���� ������ ����� �������� ����鿡�� ���� �Ѹ�
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

	// �������� ������ ä�ø�Ͽ��� �������� �Ѵ�.
	public void getAccessedEmp() {
		AccessedEmpsize = Smain.clientList.size();
		// ������ ����� ���̵� �����Ѵ�.
		AccessedEmps = new String[AccessedEmpsize];
		MainData rData = new MainData();
		ChatData temp = new ChatData();
		// ���� ������ �ߴ��� �˱� ���ؼ� ������ ������� ���̵� �����ش�.
		rData.protocol = 0000;
		temp.EmpsNum = AccessedEmpsize;
		rData.cData = temp;
		for (int i = 0; i < AccessedEmpsize; i++) {
			// ���� ������ ����� �������� ����鿡�� ���� ǥ���Ѵ�.
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

	// ============================���°��� ������ db�������� ������ �޾ƿ���=========
	public void attendinfoProcC(MainData data) {
		try {
			System.out.println("����������");
			Smain.attendS2.setString(1, data.empno);
			Smain.rs = Smain.attendS2.executeQuery();
			System.out.println("����������");
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
			System.out.println("���� Ȯ��" + data.empno + "1");
			Smain.rs = Smain.attendEmpS.executeQuery();
			System.out.println("���� Ȯ��" + data.empno + "2");
			Smain.rs.beforeFirst();
			System.out.println("���� Ȯ��" + data.empno + "3");
			while (Smain.rs.next()) {
				System.out.println("���� Ȯ��" + data.empno + "4");
				empno = Smain.rs.getString("EMPNO");
				name = Smain.rs.getString("NAME");
				cdept = Smain.rs.getString("CDEPT");
				crank = Smain.rs.getString("CRANK");
				System.out.println("�������� ��� empno :" + empno + "\t" + name + "\t" + cdept + "\t" + crank);
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
			System.out.println("����Ȯ�� �����ȣ: " + data.empno);
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "���");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC2(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "�ܱ�");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC3(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "����");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC4(MainData data) {
		try {

			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "�߱�");

			Smain.attendS.execute();

			attendinfoProcC(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void attendInProcC5(MainData data) {
		try {
			Smain.attendS.setString(1, data.empno);
			Smain.attendS.setString(2, "���");

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
			System.out.println("SignClientThread �α��� �����Լ�");
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
				// �α��ο� ���������Ƿ� �� Ŭ���̾�Ʈ�� ���� �߿���
				// �� ����� �̸��� ����Ѵ�.
			} else {
				isSuccess = false;
			}
		} catch (Exception e) {
		} finally {
			Smain.db.close(Smain.rs);
		}
		// �����Ѵ�.
		// ���� ���� Ŭ������ ���� ������ �ϵ��� �Ѵ�.
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
			System.out.println("passFindProc()����1" + data.empno + "  " + data.email);
			Smain.findpassS.setString(1, data.empno);
			Smain.findpassS.setString(2, data.email);
			Smain.rs = Smain.findpassS.executeQuery();
			System.out.println("passFindProc()����2");
			if (Smain.rs.next()) {
				empno = Smain.rs.getString("empno");
				name = Smain.rs.getString("name");
				pw = Smain.rs.getString("password");
				email = Smain.rs.getString("email");
				System.out.println("passFindProc()����3");
				System.out.println("empno: " + empno);
				System.out.println("name: " + name);
				System.out.println("password: " + pw);
				System.out.println("email: " + email);
				System.out.println("passFindProc()����4");
				isSuccess = true;
				// ��й�ȣ�� ã�Ҵ�..
			} else {
				isSuccess = false;
			}
		} catch (Exception e) {
		} finally {
			Smain.db.close(Smain.rs);
		}
		// �����Ѵ�.
		// ���� ���� Ŭ������ ���� ������ �ϵ��� �Ѵ�.
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
			System.out.println("������ ��� �μ�" + data.cdept);
			Smain.attendManaS.setString(1, data.cdept);
			Smain.rs = Smain.attendManaS.executeQuery();
			allWorkerSize = SP.getResultSetSize(Smain.rs);
			System.out.println("���̺� ������" + allWorkerSize);
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
			System.out.println("---------�������� ���� ����");
			noticeSize = SP.getResultSetSize(Smain.rs);
			System.out.println("-------���� ��" + noticeSize);
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
			System.out.println("����� ���� �����ͺ��̽��� ���� ��");
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
		System.out.println("-------��������" + rData.protocol);
		NoticeData temp = new NoticeData();
		System.out.println("--------����" + tempT);
		System.out.println("--------����" + tempC);
		System.out.println("--------�̸�" + tempN);
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
				case 1401: // ��ȭ��û
					sendMsg(data);
					break;
				case 1302: // �� ����
					exitProc(data);
					break;
				case 1501: // ���ε�
					upProc(data);
					break;
				case 1601: // ���� ���
					listProc(data);
					break;
				case 1602: // �ٿ�ε�
					downProc(data);
					break;
				case 1701: // �޽����� ������ ���� ���� ip���
					getIP(data);
					break;
				case 9999: // �α׾ƿ��Ҷ� �ڽ��� ������Ͽ��� ����
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
					System.out.println("-------�۾���â �Լ� ������");
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
					loginProc(data); // 1101 : �α���ó��
					getAccessedEmp(data); // �Կ� (���������� ������)
					break;
				case 1102:
					passFindProc(data); // 1102 : ���ã��
					break;
				case 2000: // ������Ȳ ������û
					attendinfoProcC(data);
					attendEmpProcC(data);
					break;
				case 2101: // ��ٽð� �������԰� ���ÿ� ������Ȳ ������û
					attendInProcC1(data);
					break;
				case 2102: // �ܱٽð� �������԰� ���ÿ� ������Ȳ ������û
					attendInProcC2(data);
					break;
				case 2103: // ����ð� �������԰� ���ÿ� ������Ȳ ������û
					attendInProcC3(data);
					break;
				case 2104: // �߱ٽð� �������԰� ���ÿ� ������Ȳ ������û
					attendInProcC4(data);
					break;
				case 2105: // ��ٽð� �������԰� ���ÿ� ������Ȳ ������û
					attendInProcC5(data);
					break;
				case 3000: // ȸ�ǽ� ����: ����, DB�κ���, ȸ�ǽ� ���� ��Ȳ ������ �ҷ��ͼ� table�� �ִ´�.
					System.out.println(data);
					System.out.println("ClientThread: Protocol 3000 in.");
					RoomEnterProc(data);
					System.out.println("ClientThread: Protocol 3000 out.");
					break;
				case 3002: // ȸ�ǽ� ����: �޺� ���� ���� �ÿ�, �ش� ���� ���� ��⸦ �󺧿� ǥ��.
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
			// ������ ȸ���� ������ ���� ��Ͽ��� ���� ���Ѿ���
			getAccessedEmp();

		}
	}
}
