package SignClient.SignIn;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import NetData.*;
import SignClient.SignIn.SignLogin.ButtonEvent;
import javautil.CJFrame;
import javautil.EPanel;
import SignMainFrame.MainFrame;

public class SignIdFind extends JDialog {
	MainFrame main;
	ImageIcon img;
	JTextField idF, emailF;
	JButton btn1;
	
	//프렝미 사이즈를 알아내기 위한 변수
	Dimension frameSize = this.getSize();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	//아이디와 비밀번호를 찾기 위한 변수
	PreparedStatement	pstmt , findS;
	ResultSet			rs;
	SignIdFind(MainFrame m) {
		super(m);
		main = m;
		this.setTitle("회원가입");
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.setBackground(Color.white);
		img = new ImageIcon("src/images/client/sign/passkakao.gif");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image icon = toolkit.getImage("src/images/client/sign/Img_PassFind.png");
		
		JPanel p5 = new JPanel(new GridLayout(3, 1));
		p5.setBackground(Color.white);

		idF = new JTextField("사원번호를 입력해주세요");
		emailF = new JTextField("이메일을 입력해주세요");

		btn1 = new JButton("비밀번호 찾기");
		p5.add(idF);
		p5.add(emailF);
		p5.add(btn1);

		
		JPanel p10 = new JPanel();
		JLabel logoLabel = new JLabel(img);
		p10.add(logoLabel);
		p10.setBackground(Color.white);

		JPanel spaceW = new EPanel(80, 80);
		JPanel spaceE = new EPanel(80, 80);

		JPanel p1 = new JPanel(new BorderLayout());

		p1.add("Center", p5);
		p1.add("North", p10);
		p1.add("West", spaceW);
		p1.add("East", spaceE);

		// 버튼 이벤트 등록
		ButtonEvent evt = new ButtonEvent();
		btn1.addActionListener(evt);
		idF.addMouseListener(new MouseEvent01());
		emailF.addMouseListener(new MouseEvent02());
		

		this.add(p1);
		this.setResizable(false);
		this.setIconImage(icon);
		this.setLocation(((screenSize.width)/2)+250, ((screenSize.height)/2)-175);
		this.setSize(500, 350);
		this.setVisible(true);
	}
//====================================================
	class MouseEvent01 extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(idF.getText().equals("사원번호를 입력해주세요")){
			idF.setText("");
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(idF.getText().equals("")){
				idF.setText("사원번호를 입력해주세요");
			}
		}
	}
//====================================================
//====================================================
	class MouseEvent02 extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		System.out.println(emailF.getText());
		System.out.println(emailF.getText().trim());

		if(emailF.getText().equals("이메일을 입력해주세요")){
			emailF.setText("");
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(emailF.getText().equals("")){
			emailF.setText("이메일을 입력해주세요");
			}
		}
	}

//====================================================
	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object target = e.getSource();
			if(target instanceof JTextField){
				idF.setText("");
				emailF.setText("");
			}else{ // 버튼일 때
				JButton temp = (JButton) e.getSource();
				if(temp == btn1){
					//할일
					//		아이디와 비번을 알아낸다.
					String empno = idF.getText();
					String email = emailF.getText();
					//	전송할 데이터의 무결성 검사를 한다.
					if(idF.getText() == null || idF.getText().length() == 0|| idF.getText().equals("사원번호를 입력해주세요")) {
						int kind = JOptionPane.showConfirmDialog(null, "사원번호가 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
						if (kind == JOptionPane.YES_OPTION) {
						return;
						}
					}
					if(emailF.getText() == null || emailF.getText().length() == 0 || emailF.getText().equals("이메일을 입력해주세요")) {
						int kind = JOptionPane.showConfirmDialog(null, "이메일이 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
						if (kind == JOptionPane.YES_OPTION) {
						return;
						}
					}
					//알아낸 정보를 클래스에 넣어서 서버에 보낸다.
					MainData	data = new MainData();
					data.protocol = 1102;	//비번 찾기
					data.empno = empno.trim().toUpperCase();
					data.email = email.trim().toLowerCase();
					System.out.println("사원번호:"+data.empno+"\t이메일:"+data.email);
					try {
						main.oos.writeObject(data);
					}
					catch(Exception e1) {
						System.out.println("비번찾기 에러: " + e1);
					}
				}
			}
		}
	}
	class CloseEvent extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			closeProc();
			main.logFind.setVisible(false);
			main.logFind.dispose();
		}
	}
	//=====================================================================

	//*=====================함수=================
	public void closeProc(){
		System.out.println("취소");
		this.setVisible(false);
		this.dispose();
	}
	//=====================함수=================
//	public static void main(String[] args) {
//		new SignIdFind();
//	}
}
