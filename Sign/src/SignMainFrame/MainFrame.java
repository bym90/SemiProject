package SignMainFrame;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
import java.net.*;
import java.awt.event.*;
import NetData.*;
import SignClient.SignAttendance.AttendanceForm;
import SignClient.SignChat.ChatCard;
import SignClient.SignChat.DownloadDlg;
import SignClient.SignIn.*;
import SignClient.SignNotice.SignNotice;
import SignClient.SignNotice.SignNoticeWrite;
import SignClient.SignRoomReserve.*;
import SignServer.SignServer;
import SignClient.Tray.TrayIconApp;
import SignThread.SignClientThread;
import SignThread.SignReceiveThread;
import javautil.EPanel;

public class MainFrame extends JFrame implements ActionListener {

	JPanel infoP;
	public JButton addEmp, geuntae, notice, roomReserve, groupChat, sendTxt, logoutB, exitB;
	Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	public Socket socket;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;

	public AttendanceForm af;
	
	public SignNotice sn;
	// ��� ��ư�� ���ȴ��� ������ �����ϴ� ����
	public String buttonInfo;
	// ���õ� ���� ������ �����ϱ� ���� ����
	public Integer nnum;
	// Ŭ���� ������ �����ϱ� ���� ����

	public RoomReserve RoomReserForm;

	public ChatCard chat;
	// ������ ������ ����
	public String room;
	// �̰Ÿ� �α��� �ÿ� �޵��� ����
	String empName;
	public DownloadDlg dDlg;
	public SignLogin log;
	public SignIdFind logFind;
	public String empno;
	public String cdept;
	public String crank;
	public String name;
	public String title;
	public String content;

	public String birth;
	public String ip;
	public String domain;
	public String loginState;

	public JLabel empnoL;
	public JLabel nameL;
	public JLabel crankL;
	public JLabel cdeptL;
	public JLabel birthL;
	public JLabel ipL;
	public JLabel domainL;
	public JLabel loginStateL;
	/** ������ ȹ�� **/
	public Dimension frameSize = this.getSize();
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/** ������ ��� **/
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("src/images/client/sign/Img_Login.png");

	/** �ð��� �������� **/
	Calendar calendar1 = Calendar.getInstance();
	public int hour = calendar1.get(Calendar.HOUR_OF_DAY);
	int min = calendar1.get(Calendar.MINUTE);
	int sec = calendar1.get(Calendar.SECOND);

	javax.swing.Timer timer;
	JLabel timeL;

	public MainFrame() {
		super("Sign 1.0ver");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		;
		this.setLayout(new BorderLayout());
		log = new SignLogin(this);
		log.setVisible(true);

		RoomReserForm = new RoomReserve(this);
		sn = new SignNotice(this);

		af = new AttendanceForm(this);
		infoP = new JPanel(new GridLayout(6, 1));
		infoP.setBackground(Color.WHITE);

		addEmp = new JButton("��� ���");
		geuntae = new JButton("���� ����");
		notice = new JButton("���� ����");
		roomReserve = new JButton("ȸ�ǽ� ����");
		groupChat = new JButton("�׷� ��ȭ�� & ����");
		logoutB = new JButton("�α׾ƿ�");
		exitB = new JButton("�����ϱ�");

		/** �ð� �߰� **/
		timer = new javax.swing.Timer(1000, this);
		timer.setInitialDelay(0);
		timer.start();

		timeL = new JLabel();

		JLabel iconL = new JLabel(new ImageIcon("src/images/client/common/mainframe_img.gif"));

		JPanel TimerP = new JPanel(new BorderLayout());
		TimerP.setBackground(Color.WHITE);
		TimerP.add("East", timeL);
		TimerP.add("West", iconL);

		System.out.println("�����ӻ�����>" + frameSize + "/t" + screenSize);

		infoP.add(geuntae);
		infoP.add(notice);
		infoP.add(roomReserve);
		infoP.add(groupChat);
		infoP.add(logoutB);
		infoP.add(exitB);

		empnoL = new JLabel();
		nameL = new JLabel();
		crankL = new JLabel();
		cdeptL = new JLabel();
		birthL = new JLabel();
		ipL = new JLabel();
		domainL = new JLabel();
		loginStateL = new JLabel();

		// ��Ʈ�� ��������
		empnoL.setFont(font);
		nameL.setFont(font);
		crankL.setFont(font);
		cdeptL.setFont(font);
		birthL.setFont(font);
		ipL.setFont(font);
		domainL.setFont(font);
		loginStateL.setFont(font);

		JPanel labelP = new JPanel(new GridLayout(8, 1));
		labelP.setBackground(Color.white);
		labelP.add(empnoL);
		labelP.add(nameL);
		labelP.add(crankL);
		labelP.add(cdeptL);
		labelP.add(birthL);
		labelP.add(ipL);
		labelP.add(domainL);
		labelP.add(loginStateL);

		JPanel northP = new EPanel(100, 70);
		JPanel westP = new EPanel(20, 100);

		JPanel pan1 = new JPanel(new FlowLayout());
		pan1.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), "��� ����"));
		pan1.setBackground(Color.white);
		pan1.add(labelP);

		this.add(BorderLayout.NORTH, TimerP);
		this.add(BorderLayout.EAST, infoP);
		this.add(BorderLayout.WEST, pan1);

		// �̺�Ʈ ���
		ButtonEvent evt = new ButtonEvent();
		addEmp.addActionListener(evt);
		groupChat.addActionListener(evt);
		geuntae.addActionListener(evt);
		notice.addActionListener(evt);
		roomReserve.addActionListener(evt);
		logoutB.addActionListener(evt);
		exitB.addActionListener(evt);

		// ������ ���
		this.setIconImage(icon);

		this.getContentPane().setBackground(Color.white);
		this.setSize(320, 320);
		this.setVisible(false);

		try {

			socket = new Socket("192.168.0.7", 4444);
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);
			System.out.println("MainFrame: Streams are made."); // ġ��

			SignReceiveThread t = new SignReceiveThread(this);
			System.out.println("MainFrame: SignReceiveThread t made."); // ġ��
			t.start();
			System.out.println("MainFrame: SignReceiveThread is executed."); // ġ��
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		++sec;
		Calendar calendar2 = Calendar.getInstance();
		hour = calendar2.get(Calendar.HOUR_OF_DAY);
		min = calendar2.get(Calendar.MINUTE);
		sec = calendar2.get(Calendar.SECOND);
		timeL.setText("���� : " + hour + "��" + min + "�� " + sec + "��");
		timeL.setFont(font);
	}

	/***********************
	 ***** �Լ� ���� ����*****
	 ************************/
	// *==================================
	public void close() {

		try {
			ois.close();
			oos.close();
			socket.close();
		} catch (Exception e) {
		} finally {
			System.exit(0);
		}
	}

	// ==================================
	// *==================================
	public void logoutProc() {
		int kind = JOptionPane.showConfirmDialog(null, "�α׾ƿ� �Ͻðڽ��ϱ�?", "�α׾ƿ�", JOptionPane.OK_CANCEL_OPTION, 0,
				new ImageIcon("src/images/client/common/logoutB.png"));
		if (kind == JOptionPane.YES_OPTION) {
			TrayIconApp.m_tray.remove(TrayIconApp.m_ti);
			MainData data = new MainData();
			// �α׾ƿ��Ҷ� ä�ù濡�� �ڽ��� ���Ž��Ѿ� �Ѵ�.(�Կ�)
			data.protocol = 9999;
			try {
				oos.writeObject(data);
			} catch (Exception e) {
			}
			this.dispose();
			this.setVisible(false);
			new MainFrame();
		}
	}

	// ==================================
	public void exitProc() {
		int kind = JOptionPane.showConfirmDialog(null, "���� �Ͻðڽ��ϱ�?", "���α׷� ����", JOptionPane.OK_CANCEL_OPTION, 0,				new ImageIcon("src/images/client/common/exitB.png"));
		if (kind == JOptionPane.YES_OPTION) {
			TrayIconApp.m_tray.remove(TrayIconApp.m_ti);
			System.exit(0);
		}
	}

	// *==================================
	public void geuntaeProc() {
		if (crank.equals("����") || crank.equals("����")) {
			af.attendInfoB.setVisible(true);
			af.attendSelfB.setVisible(true);
		} else {
			af.attendInfoB.setVisible(false);
			af.attendSelfB.setVisible(false);
		}
		MainData data = new MainData();
		data.protocol = 2000;
		data.empno = empno;
		try {
			oos.writeObject(data);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			oos.writeObject(data);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void noticeProc() {
		MainData data = new MainData();
		data.protocol = 4000;
		try {
			oos.writeObject(data);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void roomReserveProc() {
		if (crank.equals("����") || crank.equals("����")) {
			RoomReserForm.approveB.setVisible(true);
			RoomReserForm.rejectB.setVisible(true);
			RoomReserForm.deleteB.setVisible(true);
		} else {
			RoomReserForm.approveB.setVisible(false);
			RoomReserForm.rejectB.setVisible(false);
			RoomReserForm.deleteB.setVisible(false);
		}
		MainData data = new MainData();
		data.protocol = 3000;
		System.out.println("MainFrame: protocol inserted to MainData.protocol.");
		try {
			oos.writeObject(data);
			System.out.println("MainFrame: Data bean sent through ObjectOutputStream.writeObject method successfully.");
		} catch (Exception e1) {
			System.out.println("MainFrame, Error: Failed to writeObject");
			e1.printStackTrace();
		}
	}

	public void communityProc() {
		MainData data = new MainData();
		ChatData temp = new ChatData();

		temp.room = cdept;
		data.protocol = 1001;
		// ������ �ο��� �˱����ؼ� empid�� ������ ������.
		data.empno = empno;
		data.cData = temp;
		try {
			oos.writeObject(data);

		} catch (Exception e1) {
			System.out.println("���� = " + e1);
		}
	}

	// ==================================
	// *==================================
	public static void main(String[] args) {
		try {
			// UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		} catch (Exception e) {
			System.out.println("theme error:" + e);
		}
		new MainFrame();
	}
	// ==================================

	/*
	 * ==================================* =========�̺�Ʈ �κ� ����===========*
	 * ====================================
	 */
	// *==================================
	class CloseEvent extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			close();
		}
	}

	// ==================================
	// *==================================
	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == addEmp) {
				SignUpClient SignIn = new SignUpClient();
				SignIn.setSize(420, 420);
				SignIn.setVisible(true);
				SignIn.setResizable(false);
			}
			// ======================���°��� ���� ������ �������� ����=====================
			else if (target == geuntae) {
				geuntaeProc();
			} else if (target == notice) {
				noticeProc();
				// ================================= ġ��
				// =====================================//ȸ�ǽ� ���� ��Ȳ ��û.
			} else if (target == roomReserve) {
				roomReserveProc();
			} else if (target == logoutB) {
				logoutProc();
			} else if (target == exitB) {
				exitProc();
			} else { // ��ȭ&���� ä�ù�
				communityProc();
			}
		}
	}
	// ==================================
	/*
	 * ==================================* =========�̺�Ʈ �κ� ����===========*
	 * ====================================
	 */

}
