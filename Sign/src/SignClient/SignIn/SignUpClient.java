package SignClient.SignIn;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

import javautil.*;
import sun.misc.BASE64Encoder;
public class SignUpClient extends JDialog{
//public class SignUpClient extends CJFrame{ 
								//상황에 따라 프레임이 바뀔 수 있다.
	public static JTextField empnoF,nameF;
	public JTextField birthF,telF,emailF;
	public JComboBox genC, cdeptC, crankC;
	public JButton signB, cancelB;
	public JPasswordField passwordF;
	public Socket 				socket;
	public String ip;

	MyJDBC 				db;
	Connection 			con;
	//allS는 테스트 변수
	PreparedStatement	pstmt , allS;
	ResultSet			rs;

	public SignUpClient() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLayout(new BorderLayout());
		this.setTitle("회원가입");

		setInput();
		setButton();

		ButtonEvent evt = new ButtonEvent();
		signB.addActionListener(evt);
		cancelB.addActionListener(evt);

		empnoF.addFocusListener(new TextFocusEvent1());
		birthF.addFocusListener(new TextFocusEvent2());
		telF.addFocusListener(new TextFocusEvent3());
		emailF.addFocusListener(new TextFocusEvent4());
		setDB();
		this.setLocation(((screenSize.width)/2)+250, ((screenSize.height)/2)-175);
		this.setSize(420,420);
		this.setVisible(true);
		this.setResizable(false);
	}
//	public static void main(String[] args) {
//		new SignUpClient();
//	}

/*
 * 이벤트 모음 시작
 */
	//*=======================================================
	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//	단추가 4개 이므로 구분해서 처리해야 한다.
			JButton	target = (JButton) e.getSource();
			if(target == signB) {
				int kind = JOptionPane.showConfirmDialog(null, "입력한 정보가 맞습니까?", "회원가입", JOptionPane.OK_CANCEL_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
				if (kind == JOptionPane.YES_OPTION) {
					signProc();
					
					//회원가입 하면 가입환영 E-mail 전송
					SignToSendMail.recipient = emailF.getText();
					SignToSendMail.subject = "(주)경영기술개발원 Sign 가입을 축하합니다..";
					SignToSendMail.body =SignUpClient.nameF.getText()+"님 가입을 축하드립니다. "
							+ "\n발급받으신 사원번호는 "+SignUpClient.empnoF.getText().toUpperCase()+"입니다. 감사합니다.\n"
							+ "다른 문의사항이 있다면 관리자에게 문의하시기 바랍니다. 감사합니다.\n"
							+ "=====================================";
					
					SignToSendMail.filename="src/images/welcome.gif";
					closeProc();
					try {
						new SignToSendMail();
					} catch (Exception e2) {
						System.out.println(e2);
					}
				}else{
//					validationProc();
				}
			}
			else{
				closeProc();
			}
		}
	}
	public void validationProc(){
		if(empnoF.getText() == null || empnoF.getText().length() == 0) {
			int kind = JOptionPane.showConfirmDialog(null, "사원번호가 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(nameF.getText() == null || nameF.getText().length() == 0) {
			int kind = JOptionPane.showConfirmDialog(null, "이름이 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(passwordF.getPassword() == null || passwordF.getPassword().toString().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "비밀번호가 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(birthF.getText() == null || birthF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "생일이 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(telF.getText() == null || telF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "생일이 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(emailF.getText() == null || emailF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "생일이 잘못되었습니다.", "경고", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		
	}
	//=======================================================

	//*=======================================================
	class CloseEvent extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			db.close(pstmt);
			//테스트
			db.close(allS);
			db.close(con);
		}
	}

	//=======================================================
	class TextFocusEvent1 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean empnoCheck = JUtil.isEmpNo(empnoF.getText().trim());
				if(empnoCheck != true){
					JOptionPane.showMessageDialog(
							null, new JLabel(""
									+ "<html>"
									+ "<body>"
									+ "<font color='red'>사원번호 형태가 아닙니다.</font><br/>"
									+ "형태 : 영업S, 마케팅M, 기획P, 개발D<br/>"
									+ "S숫자4개, M숫자4개, P숫자4개, D0000숫자4개"
									+ "</body>"
									+ "</html>)"), "경고!!", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	class TextFocusEvent4 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean emailCheck = JUtil.isEmail(emailF.getText().trim());
			if(emailCheck != true){
				JOptionPane.showMessageDialog(
					null, new JLabel("<html>"
								+ "<body><font color='red'>이메일 형태가 아닙니다.</font><br/>"
								+ "형태 : abc@abc.co.kr"
								+ "</body></html>)"), "경고!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class TextFocusEvent3 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean telCheck = JUtil.isPhone(telF.getText().trim());
			if(telCheck != true){
				JOptionPane.showMessageDialog(
						null, new JLabel("<html><body><font color='red'>전화번호 형태</font>가 아닙니다.)<br/>"
								+ "형태 : 010-0000-0000</body></html>"), "경고!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class TextFocusEvent2 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean juminCheck = JUtil.isBirth(birthF.getText().trim());
			if(juminCheck != true){
				//생년월일
				JOptionPane.showMessageDialog(
							null, new JLabel("<html><body><font color='red'>생년월일 형태</font>가 아닙니다.<br/>"
									+ "형태 : 1999-12-12"
									+ "</body></html>)"), "경고!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
/*
 * 이벤트 모음 종료
 */


/*
 * 함수 모음 시작
 */
	//*=======================================================
	public void setDB() {
		db = new MyJDBC();
		con = db.getCon();
		//	스테이트먼트는 각각 자신이 담당할 전용 스테이트먼트로
		//	만들기로 약속했다.
		//	각각의 전용 스테이트먼트를 만들어 놓자.
		//	1.	전체 검색
		String	sql = "insert into company values( ?, ?, ?, ?, ?, ?, ?, ?, ?,null,null,sysdate,?)";
//		String	sql = "insert into company1 values( ? )";
		pstmt = db.getPstmt(sql, con);
		}//setDB
	//=======================================================

	//*=======================================================
	//데이터를 추가할 전담 함수
	public void signProc() {
		//	할일
		//		1.	입력상자에 입력된 내용을 알아낸다.
		String	empno = empnoF.getText().trim().toUpperCase();
		String	name = nameF.getText().trim();
		char[]	pass2 = passwordF.getPassword();
		String	pass3 = new String(pass2, 0, pass2.length);
		if(pass3 == null || pass3.length() == 0) {
			return;
		}
		String 	pass = new String(pass3);
		String	gen = (String)genC.getSelectedItem();
		String	birth = birthF.getText().trim();
		String	tel = telF.getText().trim();
		String	email = emailF.getText().trim();
		String	cdept = (String)cdeptC.getSelectedItem();
		String	crank = (String)crankC.getSelectedItem();
		
		
		try{
		InetAddress local = InetAddress.getLocalHost();
		ip = local.getHostAddress();
		}catch(Exception e){}

		//비밀번호 암호화해서 DB에 넣자
		BASE64Encoder encoder = new BASE64Encoder();
		String encrypPass = encoder.encode(pass.getBytes());

		System.out.println("사원번호: "+empno);
		System.out.println("이름: "+name);
		System.out.println("일반 암호:"+pass+" 암호화: "+encrypPass);
		System.out.println("성별: "+gen);
		System.out.println("생년월일:"+birth);
		System.out.println("전화번호:"+tel);
		System.out.println("이메일: "+email);
		System.out.println("부서: "+cdept);
		System.out.println("직급 :"+crank);
		System.out.println("접속ip:"+ip);

		try {
			//	2.	그 내용을 데이터베이스에게 보낸다.
			pstmt.setString(1, empno.toUpperCase());
			pstmt.setString(2, name);
			pstmt.setString(3, encrypPass);
			pstmt.setString(4, gen);
			pstmt.setString(5, birth);
			pstmt.setString(6, tel);
			pstmt.setString(7, email);
			pstmt.setString(8, cdept);
			pstmt.setString(9, crank);
			pstmt.setString(10,ip);

			//3.	질의 명령 실행한다.
			int query = pstmt.executeUpdate();
//			pstmt.execute();
			System.out.println("쿼리 실행여부:"+query);
		}
		catch(Exception e) {
			System.out.println("등록 에러 = " + e);
		}
	}
	//=======================================================


	//*=======================================================
	//테스트 함수
	public void allProc() {
		try {
			allS.setString(1, "SMITH");
			rs = allS.executeQuery();
			while(rs.next()) {
				int e1 = rs.getInt("empno");
				String e2 = rs.getString("ename");
				String e3 = rs.getString("job");
				String e4 = rs.getString("mgr");
				String e5 = rs.getString("hiredate");

				System.out.println(e1+"\t"+e2+"\t"+e3+"\t"+e4+"\t"+e5);
			}
		}
		catch(Exception e) {
			System.out.println("검색Error: " + e);
		}
	}
	//=======================================================

	//*=======================================================
	public void closeProc(){
		System.out.println("취소");
		this.setVisible(false);
		this.dispose();
	}

	//=======================================================

	//*=======================================================
	//버튼 관련 함수
	public void setButton() {
		signB = new JButton("회원가입");
		cancelB = new JButton("취소하기");
		JPanel p14 = new JPanel(new FlowLayout());

		p14.add(signB);
		p14.add(cancelB);
		p14.setBackground(Color.white);
		this.add("South",p14);
		}
	//======================================================



	//*======================================================
	public void setInput(){
		JLabel empnoL = new JLabel("사원번호: ");
		empnoF = new JTextField();

		JLabel nameL = new JLabel("이름: ");
		nameF = new JTextField();

		JLabel passwordL = new JLabel("비밀번호: ");
		passwordF = new JPasswordField();
		passwordF.setEchoChar('●');

		JLabel genL = new JLabel("성별: ");
		genC = new JComboBox();
		genC.addItem("남");
		genC.addItem("여");
		genC.setBackground(Color.WHITE);

		JLabel birthL = new JLabel("생년월일: ");
		birthF = new JTextField();

		JLabel telL = new JLabel("핸드폰: ");
		telF = new JTextField();

		JLabel emailL = new JLabel("이메일: ");
		emailF = new JTextField();

		JLabel cdeptL = new JLabel("부서: ");
		cdeptC = new JComboBox();
		cdeptC.addItem("마케팅부"); //Marketing
		cdeptC.addItem("기획부"); //기획부
		cdeptC.addItem("영업부"); //Sales
		cdeptC.addItem("개발부"); //Develope

		JLabel crankL = new JLabel("직급: ");
		crankC = new JComboBox();
		this.crankC.addItem("사원");
		this.crankC.addItem("주임");
		this.crankC.addItem("대리");
		this.crankC.addItem("과장");
		this.crankC.addItem("부장");

		JPanel p9 = new JPanel(new GridLayout(9,2));
		p9.add(empnoL);
		p9.add(empnoF);
		p9.add(nameL);
		p9.add(nameF);
		p9.add(passwordL);
		p9.add(passwordF);
		p9.add(genL);
		p9.add(genC);
		p9.add(birthL);
		p9.add(birthF);
		p9.add(telL);
		p9.add(telF);
		p9.add(emailL);
		p9.add(emailF);
		p9.add(emailL);
		p9.add(emailF);
		p9.add(cdeptL);
		p9.add(cdeptC);
		p9.add(crankL);
		p9.add(crankC);
		p9.setBackground(Color.white);

		JPanel p16 = new EPanel(10, 10);
		JPanel p17 = new EPanel(10, 10);
		JPanel p18 = new EPanel(10, 10);
		JPanel p19 = new EPanel(10, 10);


		JPanel p15 = new JPanel(new BorderLayout());
		p15.add("Center",p9);
		p15.add("West",p16);
		p15.add("East",p17);
		p15.add("North",p18);
		p15.add("South",p19);
		p15.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		p15.setBorder(new LineBorder(Color.BLACK, 2));


		JPanel p11 = new JPanel();
		p11.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "회원가입"));
		p11.add(new JLabel("신입사원 중 사원번호 미발급시 관리자에게 문의하세요", JLabel.CENTER));
		JPanel p12 = new EPanel(20, 40);
		JPanel p13 = new EPanel(20, 40);

		this.add("Center",p15);
		//공백 넣기
		this.add("North",p11);
		this.add("West",p12);
		this.add("East",p13);
	}
	//======================================================


}

