package SignClient.SignIn;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SignMainFrame.MainFrame;
import javautil.CJFrame;
import javautil.EPanel;

import NetData.*;
public class SignLogin extends CJFrame{
	MainFrame main;
    ImageIcon img;
    JTextField idF;
    JPasswordField pwF;
    JButton btn1, signB,findB;
    
    /**사이즈 획득**/
    Dimension frameSize = this.getSize();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**아이콘 등록**/
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("src/images/client/sign/Img_Login.png");
	
	public SignLogin(MainFrame m){ //MainFrame m 괄호안에
		super("Sign1.00 ver");
		main = m;
		img = new ImageIcon("src/images/client/common/login.gif");

		signB = new JButton("사원등록");
		findB = new JButton("비밀번호 찾기");
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		p2.setBackground(Color.white);
		p2.add(signB);
		p2.add(findB);
		
		JPanel p5 = new JPanel(new GridLayout(4,1));
		p5.setBackground(Color.white);
		idF = new JTextField("사원번호를 입력해주세요");
		pwF = new JPasswordField();
		pwF.setEchoChar('●');
		
		btn1 = new JButton("로그인");
		p5.add(idF);
		p5.add(pwF);
		p5.add(btn1);
		p5.add(p2);
		
		JPanel p10 = new JPanel();
		JLabel	logoLabel = new JLabel(img);
		p10.add(logoLabel);
		p10.setBackground(Color.white);
		
		JPanel spaceW = new EPanel(80,80);
		JPanel spaceE = new EPanel(80,80);
		

		JPanel p1 = new JPanel(new BorderLayout());
		
		p1.add("Center",p5);
		p1.add("North",p10);
		p1.add("West",spaceW);
		p1.add("East",spaceE);
		
		//버튼 이벤트 등록
		ButtonEvent evt = new ButtonEvent();
		btn1.addActionListener(evt);
		signB.addActionListener(evt);
		findB.addActionListener(evt);
		
		//필드 이벤트 등록
		idF.addMouseListener(new MouseEvent01()); 

		this.add(p1);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setIconImage(icon);
//		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		this.setLocation(((screenSize.width)/2)-250, ((screenSize.height)/2)-175);
		this.setSize(500,350);
		this.setVisible(true);
		
		
	}
//******************************************************
	//*====================================================
	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if(target == btn1){//로그인
//				할일
				//		아이디와 비번을 알아낸다.
				String	empno = idF.getText();
				//	전송할 데이터의 무결성 검사를 한다.
				if(empno == null || empno.length() == 0) {
					return;
				}
				char[]	tempPW = pwF.getPassword();
				String	pw = new String(tempPW, 0, tempPW.length);
				if(pw == null || pw.length() == 0) {
					return;
				}
				System.out.println("로그인 아이디:"+empno+"\t비밀번호:"+pw);

				//		알아낸 정보를 클래스에 넣어서 서버에 보낸다. (규영도 추가했음) 
				main.empno = empno; //규영
				MainData data = new MainData();
				data.protocol = 1101;
				data.empno = empno.toUpperCase();
				data.pw = pw;
				try {
					main.oos.writeObject(data);
					System.out.println("함수들어온다");
				}
				catch (Exception e1) {
					System.out.println("에러 = " + e);
				}
			} else if (target == signB) {// 사원등록
				new SignUpClient();
				
				
			} else if (target == findB) {// 비밀번호찾기
				new SignIdFind(main);
			} else {
				System.out.println("현재는 아무버튼 없음");
			}
		}
	}
	//*====================================================
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
	class CloseEvent extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			main.log.setVisible(false);
			main.log.dispose();
		}
	}
	
//******************************************************
//	public static void main(String[] args) {
//		new SignLogin();
//	}
}
