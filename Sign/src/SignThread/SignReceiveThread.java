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

	// �����ư�� �����ÿ�, DB�� �߰��� ����, �ٽ� table�� �߰�.
	public void tableaddrowProc(MainData data) {
		System.out.println("SignReceiveThread: entered tableaddrowProc.");
		Object[] obj = data.rData.reserveTest;
		main.RoomReserForm.addOneRow(obj);
	}

	// DB���� ���� ȸ�ǽ� ������Ȳ�� ���̺� �ѷȴ�.
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
		// Ŭ���̾�Ʈ �����忡�� �޾ƿ� MainData�� ����� ChatData�� ���� �־���
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

		// ������ �� �޾ƿ��� Jtree�� �����.
		main.chat.renewProc();
		main.chat.setVisible(true);
	}

	public void showMsg(MainData data) {
		// �ؽ�Ʈ ����� ����Ѵ�.
		main.chat.area.append(data.cData.msg + "\r\n");
		main.chat.area.setCaretPosition(main.chat.area.getDocument().getLength());
	}

	public void exitProc(MainData data) {
		main.chat.setVisible(false);
	}

	public void upProc(MainData data) {
		JOptionPane.showMessageDialog(main, "���� ���ε� ����");
	}

	public void listProc(MainData data) {
		main.dDlg = new DownloadDlg(main);
		// ����� ����� ��Ͽ� ����Ѵ�.
		main.dDlg.list.setListData(data.fData.files);
		main.dDlg.setSize(350, 350);
		main.dDlg.setLocation(((screenSize.width) / 2) - 175, ((screenSize.height) / 2) - 175);
		main.dDlg.setVisible(true);
	}

	public void downProc(MainData data) {
		System.out.println("�����̸�" + data.fData.fileName);
		File file = new File("D://Download", data.fData.fileName);
		// ��Ʈ���� �̿��ؼ� ������ �����Ѵ�
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(data.fData.buff);
			JOptionPane.showMessageDialog(main.dDlg, "�ٿ�ε� �Ϸ�");
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

	// ���� db������ ���̺� �ѷ��ֱ�........
	public void attendinfoProcS(MainData data) {

		String[] temp = { "��¥", "�ð�", "����" };
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
		String[] temp = { "��¥", "�ð�", "�̸�", "�����ȣ", "����" };
		main.af.model.setDataVector(data.aData.attendWorkerAll, temp);

		main.af.setSize(600, 600);
		main.af.setVisible(true);
	}

	public void loginProc(MainData data) {
		System.out.println("�α��� ��������:" + data.isSuccess);
		if (data.isSuccess == true) {
			InetAddress ipa = null;
			try {
				ipa = InetAddress.getLocalHost();
				main.ip = ipa.getHostAddress();
				main.domain = ipa.getHostName();
				System.out.println("����IP�ּ�:" + main.ip + "���ӵ�����:" + main.domain);
			} catch (Exception e) {
				System.out.println("IP�޾ƿ��� ����:" + e);
			}
			main.empno = data.empno;
			main.cdept = data.cdept;
			main.crank = data.crank;
			main.name = data.name;
			String birthday = data.birth;
			main.birth = birthday.substring(0, 10);
			// �α��� �¶��� ���θ� ��������.
			main.loginState = "Y"; // �α��� ���¸� �־��ش�.
			// ����â�� �ٽ� �α����ϸ� ����.
			main.setLocation(0, 0);
			main.setLocation((main.screenSize.width) - 320, 0);
			main.setVisible(true);

			// �α��� â�� ���ش�.
			main.log.setVisible(false);

			// main.tray.
			TrayIconApp tray = new TrayIconApp("Sign Ver1.0 ��� ��Ʈ���", main);

			// ä�ù��� ���� �μ���ȣ ����
			main.room = data.cdept;

			main.empnoL.setText("�����ȣ: " + data.empno);
			main.nameL.setText("��       ��: " + data.name);
			main.crankL.setText("��       å: " + data.crank);
			main.cdeptL.setText("��       ��: " + data.cdept);
			main.birthL.setText("�������: " + data.birth);
			main.ipL.setText("�� �� I  P : " + main.ip);
			main.domainL.setText("���ӵ����� : " + main.domain);
			if (main.loginState.equals("Y")) {
				main.loginStateL.setText("���ӿ���: �¶���");
			} else {
				main.loginStateL.setText("���ӿ���: ��������");
			}

		} else {
			// �����ϸ� �޽����� ������� �����̴�.
			JOptionPane.showMessageDialog(main, "�α��ο� �����߽��ϴ�.");
		}
	}

	public void setAccessedEmp(MainData data) {
		// ���� ������ ������� ����Ѵ�.
		main.chat.NameEmpno = data.NameEmpno;
		main.chat.EmpsSize = data.cData.EmpsNum;
		main.chat.Emps = data.cData.Emps;
		// ������ �����ϸ� ����Ʈ�� ����
		main.chat.renewProc();
		
		if(data.NameEmpno==null){
			return;
		}
		else if(main.empno.substring(0,1).equals(
			data.NameEmpno.substring(data.NameEmpno.length()-5,data.NameEmpno.length()-4)))
		{
		main.chat.area.append(data.NameEmpno +"���� ���� �ϼ̽��ϴ�."+"\r\n");
		}
	}

	public void passFindProc(MainData data) {
		System.out.println("���ã�� ��������:" + data.isSuccess);
		System.out.println("��й�ȣ �� return:" + data.pw);
		if (data.isSuccess == true) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				// ��ȣȭ
				String decodePW = new String(decoder.decodeBuffer(data.pw));
				System.out.println("��ȣȭ �� ��ȣ:" + decodePW);
				JOptionPane.showMessageDialog(null,
						new JLabel("<html>" + "<body><font color='blue'>" + data.name + "�� ������ �ֽ��ϴ�.</font><br>"
								+ "�����ȣ: " + data.empno + "<br>" + "�����Ͻ� " + data.email + " �ּҷ� �߼�ó���Ͽ����ϴ�.<br>"
								+ "</body></html>)"),
						"��й�ȣ ã��", JOptionPane.OK_OPTION);
				try {
					// DB�� ��ȣȭ�� ��й�ȣ ��ȣȭ�Ͽ� �̸��� ����
					SignToSendMail.recipient = data.email.trim();
					SignToSendMail.subject = "(��)Sign ��й�ȣ�� �߱��Ͽ����ϴ�.";
					SignToSendMail.body = data.name + "��(" + data.empno.toUpperCase() + ")�� ��й�ȣ�� ������ �����ϴ�.\n"
							+ "��й�ȣ: " + decodePW + "\n" + "\n ������ ��û�� ������ �ƴϸ� �����ڿ��� �����Ͻñ� �ٶ��ϴ�.\n�����մϴ�.\n"
							+ "=====================================";
					SignToSendMail.filename = "src/images/pwFind.gif";
					// =======�׽�Ʈ ���
					System.out.println("SignToSendMail.recipient:-> " + SignToSendMail.recipient);
					System.out.println("SignToSendMail.subject-> " + SignToSendMail.subject);
					System.out.println("SignToSendMail.body" + SignToSendMail.body);
					new SignToSendMail();
				} catch (Exception e) {
					System.out.println("�̸��� ���� ����:" + e);
					e.printStackTrace();
				}

			} catch (Exception e) {
				System.out.println("��ȣȭ �����Ӊ�:" + e);
			}
		} else {
			// �����ϸ� �޽����� ������� �����̴�.
			JOptionPane.showMessageDialog(main, "��ġ�ϴ� ������ �����ϴ�.");
		}
	}

	public void noticeProcS(MainData data) {
		String[] temp = { "�۹�ȣ", "����", "�ۼ���", "�ۼ���" };
		main.sn.model.setDataVector(data.nData.noticeAll, temp);

		main.sn.table.getColumn("�۹�ȣ").setMaxWidth(40);
		main.sn.table.getColumn("����").setMaxWidth(400);
		main.sn.table.getColumn("�ۼ���").setMaxWidth(80);
		main.sn.table.getColumn("�ۼ���").setMaxWidth(140);

		main.sn.setSize(660, 600);
		main.sn.setVisible(true);
	}

	public void writeProcS(MainData data) {
		main.buttonInfo = data.nData.writeInfo;

		main.sn.Snw.nameL.setText("�ۼ��� : " + data.name);
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

		main.sn.Snw.nameL.setText("�ۼ��� : " + data.nData.name);
		main.sn.Snw.field.setText(data.nData.title);
		main.sn.Snw.area.setText(data.nData.content);

		main.sn.Snw.setSize(660, 300);
		main.sn.Snw.setVisible(true);

	}

	public void readProcS(MainData data) {
		main.sn.Snread.nameL.setText("�ۼ��� : " + data.nData.name);
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

				case 2102: // 2102 �α��� �Լ�
					loginProc(data);
					break;

				// ä�ù� ����
				case 2001:
					enterProc(data);
					break;

				case 2401: // �Ϲ� ��ȭ
					showMsg(data);
					break;

				case 2302: // ������
					exitProc(data);
					break;

				case 2501: // ���ε�
					upProc(data);
					break;
				case 2601: // ���
					listProc(data);
					break;
				case 2602: // �ٿ�ε�
					downProc(data);
					break;
				case 1702:

					setIP(data); // ���������� ���� ���� �������ּҸ� ����
					break;

				case 2100: // DB���� ���̺� �ѷ��ֱ�
					attendinfoProcS(data);
					break;
				case 2111:
					attendEmpProcS(data);
					break;
				case 2223:
					attendManagerS(data);
					break;
				// ===================ġ��=================//
				case 3001:
					roomReserveProc(data);
					break;
				case 3003:
					roomPropertyProc(data);
					break;
				case 3102:
					tableaddrowProc(data);
					break;
				// ===================ġ��=================//

				// �������ڸ��� ��� ������ �����ٰ� JTree�� �ѷ��ֱ�(����������)
				case 0000:
					setAccessedEmp(data);
					break;
				case 2202: // 2202 ���ã�� �Լ�
					passFindProc(data);
					break;
				}
			}
		} catch (Exception e) {		
		}
	}
}