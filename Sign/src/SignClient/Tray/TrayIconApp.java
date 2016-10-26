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
	// SystemTrayŬ������ �����´�.
	public static SystemTray m_tray = SystemTray.getSystemTray();
	// Ʈ���� ������
	public static TrayIcon m_ti;

	// Ʈ���� ������ Ÿ��Ʋ
	String m_strTrayTitle;

	public TrayIconApp(String strTrayTitle, MainFrame m) {
		main = m;
		m_strTrayTitle = strTrayTitle;
		
		initTray();
	}
	// Ʈ���� �������� �ʱ⼳���� ���ݴϴ�.
	private void initTray() {
		// Ʈ���� �������� ������ ������ �� �̹��� �Դϴ�.
		Image image = Toolkit.getDefaultToolkit().getImage("src/images/client/tray/SignTray.gif");
		// TrayIcon�� �����մϴ�.
		m_ti = new TrayIcon(image, m_strTrayTitle, createPopupMenu());
		m_ti.setImageAutoSize(true);
		m_ti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ʈ���� ������ ��ü�� Ŭ�������� �Ͼ �̺�Ʈ�� ���� ������ ����
				System.out.println("���Ŀ� â�� �߰� �Ѵ�.");
				main.setVisible(!main.isVisible());
				
			}
		});
		
		// ������ ���� SystemTray�� ��� �� ������ TrayIcon�� �ν��Ͻ��� ���ڷ� �־��ݴϴ�.
		try {
			m_tray.add(m_ti);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	// Ʈ���� �����ܿ��� ����� �˾� �Ŵ��� ����ϴ�.
	private PopupMenu createPopupMenu() {
		PopupMenu popupMenu = new PopupMenu();

		MenuItem mAttend = new MenuItem("�� �� �� ��");
		MenuItem mNotice = new MenuItem("�� �� �� ��");
		MenuItem mCommunity = new MenuItem("��ȭ & ����");
		MenuItem mRoom = new MenuItem("ȸ�ǽ� ����");
		MenuItem mLogout = new MenuItem("�� �� �� ��");
		MenuItem mQuit = new MenuItem("���α׷� ����");

		// ������ �׸� ���� ������ ����.
		mAttend.addActionListener(this);
		mNotice.addActionListener(this);
		mCommunity.addActionListener(this);
		mRoom.addActionListener(this);
		mLogout.addActionListener(this);
		
		mQuit.addActionListener(this);

		// �˾� �޴��� ���
		popupMenu.add(mAttend);//���°���
		popupMenu.addSeparator();
		popupMenu.add(mNotice);// ��������
		popupMenu.addSeparator();
		popupMenu.add(mCommunity);// ��ȭ&����
		popupMenu.addSeparator();
		popupMenu.add(mRoom);// ȸ�ǽǿ���
		popupMenu.addSeparator();
		popupMenu.add(mLogout);// �α׾ƿ�
		popupMenu.addSeparator();
		popupMenu.add(mQuit);// ���α׷� ����

		return popupMenu;
	}

/*==================================*
=========�̺�Ʈ �κ� ����===========*
====================================*/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "�� �� �� ��") {
			// ������ �׸� ���ؼ� �Ͼ �ൿ�� ���� ����
			main.geuntaeProc();
		} else if (e.getActionCommand() == "�� �� �� ��") {
			System.out.println("Ʈ����> �������� ����");
			main.noticeProc();
		}else if (e.getActionCommand() == "��ȭ & ����") {
			System.out.println("Ʈ����> ��ȭ & ���� ����");
			main.communityProc();
		}else if (e.getActionCommand() == "ȸ�ǽ� ����") {
			System.out.println("Ʈ����> ȸ�ǽ� ���� ����");
			main.roomReserveProc();
		}else if (e.getActionCommand() == "�� �� �� ��") {
			System.out.println("Ʈ����> �α׾ƿ� ����");
			logoutProc();
		} else if (e.getActionCommand() == "���α׷� ����") {
			exitProc();
		}
	}
/*==================================*
=========�̺�Ʈ �κ� ����===========*
====================================*/
/***********************
*****�Լ� ���� ����*****
************************/
	// *================================
		public void logoutProc() {
			int kind = JOptionPane.showConfirmDialog(null, "�α׾ƿ� �Ͻðڽ��ϱ�?", "�α׾ƿ�", JOptionPane.OK_CANCEL_OPTION ,0,new ImageIcon("src/images/client/common/logoutB.png"));
			if (kind == JOptionPane.YES_OPTION) {
				main.logoutProc();
				m_tray.remove(m_ti);
				
			}
		}
	//================================
		
	// *================================
	public void exitProc() {
		int kind = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "���α׷� ����", JOptionPane.OK_CANCEL_OPTION,0,new ImageIcon("src/images/client/common/exitB.png"));

		if (kind == JOptionPane.YES_OPTION) {
			System.exit(-1);
		} else if (kind == JOptionPane.NO_OPTION) {
			System.out.println("�ƴϿ� ���߸� �������ϴ�.");
		} else {
			System.out.println("��Ÿ ���߸� �������ϴ�.");
		}
	}// exitProc();
	// ======================================

	// *================================================
	// public static void main(String[] args) {
	// new TrayIconApp("Sign Ver1.0 ��� ��Ʈ���");
	// }//main
	// ================================================
/***********************
*****�Լ� ���� ����*****
************************/
}
