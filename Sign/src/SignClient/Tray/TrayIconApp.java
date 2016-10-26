package SignClient.Tray;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import SignClient.SignIn.*;
import SignClient.SignAttendance.AttendanceForm;
import SignMainFrame.*;
import NetData.MainData;

public class TrayIconApp implements ActionListener {
	MainFrame main;
	// SystemTray클래스를 가져온다.
	public static SystemTray m_tray = SystemTray.getSystemTray();
	// 트레이 아이콘
	public static TrayIcon m_ti;

	// 트레이 아이콘 타이틀
	String m_strTrayTitle;

	public TrayIconApp(String strTrayTitle, MainFrame m) {
		main = m;
		m_strTrayTitle = strTrayTitle;
		
		initTray();
	}
	// 트레이 아이콘의 초기설정을 해줍니다.
	private void initTray() {
		// 트레이 아이콘의 아이콘 역할을 할 이미지 입니다.
		Image image = Toolkit.getDefaultToolkit().getImage("src/images/client/tray/SignTray.gif");
		// TrayIcon을 생성합니다.
		m_ti = new TrayIcon(image, m_strTrayTitle, createPopupMenu());
		m_ti.setImageAutoSize(true);
		m_ti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 트레이 아이콘 자체를 클릭했을때 일어날 이벤트에 대한 동작을 구현
				System.out.println("이후에 창을 뜨게 한다.");
				main.setVisible(!main.isVisible());
				
			}
		});
		
		// 위에서 얻어온 SystemTray에 방금 막 생성한 TrayIcon의 인스턴스를 인자로 넣어줍니다.
		try {
			m_tray.add(m_ti);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	// 트레이 아이콘에서 사용할 팝업 매뉴를 만듭니다.
	private PopupMenu createPopupMenu() {
		PopupMenu popupMenu = new PopupMenu();

		MenuItem mAttend = new MenuItem("근 태 관 리");
		MenuItem mNotice = new MenuItem("공 지 사 항");
		MenuItem mCommunity = new MenuItem("대화 & 쪽지");
		MenuItem mRoom = new MenuItem("회의실 예약");
		MenuItem mLogout = new MenuItem("로 그 아 웃");
		MenuItem mQuit = new MenuItem("프로그램 종료");

		// 각각에 항목에 대해 리스너 장착.
		mAttend.addActionListener(this);
		mNotice.addActionListener(this);
		mCommunity.addActionListener(this);
		mRoom.addActionListener(this);
		mLogout.addActionListener(this);
		
		mQuit.addActionListener(this);

		// 팝업 메뉴에 등록
		popupMenu.add(mAttend);//근태관리
		popupMenu.addSeparator();
		popupMenu.add(mNotice);// 공지사항
		popupMenu.addSeparator();
		popupMenu.add(mCommunity);// 대화&쪽지
		popupMenu.addSeparator();
		popupMenu.add(mRoom);// 회의실예약
		popupMenu.addSeparator();
		popupMenu.add(mLogout);// 로그아웃
		popupMenu.addSeparator();
		popupMenu.add(mQuit);// 프로그램 종료

		return popupMenu;
	}

/*==================================*
=========이벤트 부분 시작===========*
====================================*/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "근 태 관 리") {
			// 각각의 항목에 대해서 일어날 행동에 대해 정의
			main.geuntaeProc();
		} else if (e.getActionCommand() == "공 지 사 항") {
			System.out.println("트레이> 공지사항 눌름");
			main.noticeProc();
		}else if (e.getActionCommand() == "대화 & 쪽지") {
			System.out.println("트레이> 대화 & 쪽지 눌름");
			main.communityProc();
		}else if (e.getActionCommand() == "회의실 예약") {
			System.out.println("트레이> 회의실 예약 눌름");
			main.roomReserveProc();
		}else if (e.getActionCommand() == "로 그 아 웃") {
			System.out.println("트레이> 로그아웃 눌름");
			logoutProc();
		} else if (e.getActionCommand() == "프로그램 종료") {
			exitProc();
		}
	}
/*==================================*
=========이벤트 부분 종료===========*
====================================*/
/***********************
*****함수 모음 시작*****
************************/
	// *================================
		public void logoutProc() {
			int kind = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.OK_CANCEL_OPTION ,0,new ImageIcon("src/images/client/common/logoutB.png"));
			if (kind == JOptionPane.YES_OPTION) {
				main.logoutProc();
				m_tray.remove(m_ti);
				
			}
		}
	//================================
		
	// *================================
	public void exitProc() {
		int kind = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "프로그램 종료", JOptionPane.OK_CANCEL_OPTION,0,new ImageIcon("src/images/client/common/exitB.png"));

		if (kind == JOptionPane.YES_OPTION) {
			System.exit(-1);
		} else if (kind == JOptionPane.NO_OPTION) {
			System.out.println("아니오 단추를 눌렀습니다.");
		} else {
			System.out.println("기타 단추를 눌렀습니다.");
		}
	}// exitProc();
	// ======================================

	// *================================================
	// public static void main(String[] args) {
	// new TrayIconApp("Sign Ver1.0 사원 인트라넷");
	// }//main
	// ================================================
/***********************
*****함수 모음 종료*****
************************/
}
