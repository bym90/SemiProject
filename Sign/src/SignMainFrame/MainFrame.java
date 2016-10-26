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
	// 어느 버튼이 눌렸는지 정보를 저장하는 변수
	public String buttonInfo;
	// 선택된 행의 정보를 저장하기 위한 변수
	public Integer nnum;
	// 클릭수 정보를 저장하기 위한 변수

	public RoomReserve RoomReserForm;

	public ChatCard chat;
	// 같은방 설정할 변수
	public String room;
	// 이거를 로그인 시에 받도록 수정
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
	/** 사이즈 획득 **/
	public Dimension frameSize = this.getSize();
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/** 아이콘 등록 **/
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("src/images/client/sign/Img_Login.png");

	/** 시간을 가져오자 **/
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

		addEmp = new JButton("사원 등록");
		geuntae = new JButton("근태 관리");
		notice = new JButton("공지 사항");
		roomReserve = new JButton("회의실 예약");
		groupChat = new JButton("그룹 대화방 & 쪽지");
		logoutB = new JButton("로그아웃");
		exitB = new JButton("종료하기");

		/** 시간 추가 **/
		timer = new javax.swing.Timer(1000, this);
		timer.setInitialDelay(0);
		timer.start();

		timeL = new JLabel();

		JLabel iconL = new JLabel(new ImageIcon("src/images/client/common/mainframe_img.gif"));

		JPanel TimerP = new JPanel(new BorderLayout());
		TimerP.setBackground(Color.WHITE);
		TimerP.add("East", timeL);
		TimerP.add("West", iconL);

		System.out.println("프레임사이즈>" + frameSize + "/t" + screenSize);

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

		// 폰트를 설정하자
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
		pan1.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), "사원 정보"));
		pan1.setBackground(Color.white);
		pan1.add(labelP);

		this.add(BorderLayout.NORTH, TimerP);
		this.add(BorderLayout.EAST, infoP);
		this.add(BorderLayout.WEST, pan1);

		// 이벤트 등록
		ButtonEvent evt = new ButtonEvent();
		addEmp.addActionListener(evt);
		groupChat.addActionListener(evt);
		geuntae.addActionListener(evt);
		notice.addActionListener(evt);
		roomReserve.addActionListener(evt);
		logoutB.addActionListener(evt);
		exitB.addActionListener(evt);

		// 아이콘 등록
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
			System.out.println("MainFrame: Streams are made."); // 치현

			SignReceiveThread t = new SignReceiveThread(this);
			System.out.println("MainFrame: SignReceiveThread t made."); // 치현
			t.start();
			System.out.println("MainFrame: SignReceiveThread is executed."); // 치현
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
		timeL.setText("현재 : " + hour + "시" + min + "분 " + sec + "초");
		timeL.setFont(font);
	}

	/***********************
	 ***** 함수 모음 시작*****
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
		int kind = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.OK_CANCEL_OPTION, 0,
				new ImageIcon("src/images/client/common/logoutB.png"));
		if (kind == JOptionPane.YES_OPTION) {
			TrayIconApp.m_tray.remove(TrayIconApp.m_ti);
			MainData data = new MainData();
			// 로그아웃할때 채팅방에서 자신을 제거시켜야 한다.(규영)
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
		int kind = JOptionPane.showConfirmDialog(null, "종료 하시겠습니까?", "프로그램 종료", JOptionPane.OK_CANCEL_OPTION, 0,				new ImageIcon("src/images/client/common/exitB.png"));
		if (kind == JOptionPane.YES_OPTION) {
			TrayIconApp.m_tray.remove(TrayIconApp.m_ti);
			System.exit(0);
		}
	}

	// *==================================
	public void geuntaeProc() {
		if (crank.equals("과장") || crank.equals("부장")) {
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
		if (crank.equals("과장") || crank.equals("부장")) {
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
		// 접속한 인원을 알기위해서 empid를 서버에 보낸다.
		data.empno = empno;
		data.cData = temp;
		try {
			oos.writeObject(data);

		} catch (Exception e1) {
			System.out.println("에러 = " + e1);
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
	 * ==================================* =========이벤트 부분 시작===========*
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
			// ======================근태관리 단추 누를시 프로토콜 전송=====================
			else if (target == geuntae) {
				geuntaeProc();
			} else if (target == notice) {
				noticeProc();
				// ================================= 치현
				// =====================================//회의실 예약 현황 요청.
			} else if (target == roomReserve) {
				roomReserveProc();
			} else if (target == logoutB) {
				logoutProc();
			} else if (target == exitB) {
				exitProc();
			} else { // 대화&쪽지 채팅방
				communityProc();
			}
		}
	}
	// ==================================
	/*
	 * ==================================* =========이벤트 부분 종료===========*
	 * ====================================
	 */

}
