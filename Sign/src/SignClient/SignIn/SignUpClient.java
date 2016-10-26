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
								//��Ȳ�� ���� �������� �ٲ� �� �ִ�.
	public static JTextField empnoF,nameF;
	public JTextField birthF,telF,emailF;
	public JComboBox genC, cdeptC, crankC;
	public JButton signB, cancelB;
	public JPasswordField passwordF;
	public Socket 				socket;
	public String ip;

	MyJDBC 				db;
	Connection 			con;
	//allS�� �׽�Ʈ ����
	PreparedStatement	pstmt , allS;
	ResultSet			rs;

	public SignUpClient() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLayout(new BorderLayout());
		this.setTitle("ȸ������");

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
 * �̺�Ʈ ���� ����
 */
	//*=======================================================
	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//	���߰� 4�� �̹Ƿ� �����ؼ� ó���ؾ� �Ѵ�.
			JButton	target = (JButton) e.getSource();
			if(target == signB) {
				int kind = JOptionPane.showConfirmDialog(null, "�Է��� ������ �½��ϱ�?", "ȸ������", JOptionPane.OK_CANCEL_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
				if (kind == JOptionPane.YES_OPTION) {
					signProc();
					
					//ȸ������ �ϸ� ����ȯ�� E-mail ����
					SignToSendMail.recipient = emailF.getText();
					SignToSendMail.subject = "(��)�濵������߿� Sign ������ �����մϴ�..";
					SignToSendMail.body =SignUpClient.nameF.getText()+"�� ������ ���ϵ帳�ϴ�. "
							+ "\n�߱޹����� �����ȣ�� "+SignUpClient.empnoF.getText().toUpperCase()+"�Դϴ�. �����մϴ�.\n"
							+ "�ٸ� ���ǻ����� �ִٸ� �����ڿ��� �����Ͻñ� �ٶ��ϴ�. �����մϴ�.\n"
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
			int kind = JOptionPane.showConfirmDialog(null, "�����ȣ�� �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(nameF.getText() == null || nameF.getText().length() == 0) {
			int kind = JOptionPane.showConfirmDialog(null, "�̸��� �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(passwordF.getPassword() == null || passwordF.getPassword().toString().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "��й�ȣ�� �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(birthF.getText() == null || birthF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "������ �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(telF.getText() == null || telF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "������ �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
			if (kind == JOptionPane.YES_OPTION) {
			return;
			}
		}
		if(emailF.getText() == null || emailF.getText().length()==0) {
			int kind = JOptionPane.showConfirmDialog(null, "������ �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
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
			//�׽�Ʈ
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
									+ "<font color='red'>�����ȣ ���°� �ƴմϴ�.</font><br/>"
									+ "���� : ����S, ������M, ��ȹP, ����D<br/>"
									+ "S����4��, M����4��, P����4��, D0000����4��"
									+ "</body>"
									+ "</html>)"), "���!!", JOptionPane.ERROR_MESSAGE);
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
								+ "<body><font color='red'>�̸��� ���°� �ƴմϴ�.</font><br/>"
								+ "���� : abc@abc.co.kr"
								+ "</body></html>)"), "���!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class TextFocusEvent3 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean telCheck = JUtil.isPhone(telF.getText().trim());
			if(telCheck != true){
				JOptionPane.showMessageDialog(
						null, new JLabel("<html><body><font color='red'>��ȭ��ȣ ����</font>�� �ƴմϴ�.)<br/>"
								+ "���� : 010-0000-0000</body></html>"), "���!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class TextFocusEvent2 implements FocusListener {
		public void focusGained(FocusEvent e) {}
		public void focusLost(FocusEvent e) {
			boolean juminCheck = JUtil.isBirth(birthF.getText().trim());
			if(juminCheck != true){
				//�������
				JOptionPane.showMessageDialog(
							null, new JLabel("<html><body><font color='red'>������� ����</font>�� �ƴմϴ�.<br/>"
									+ "���� : 1999-12-12"
									+ "</body></html>)"), "���!!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
/*
 * �̺�Ʈ ���� ����
 */


/*
 * �Լ� ���� ����
 */
	//*=======================================================
	public void setDB() {
		db = new MyJDBC();
		con = db.getCon();
		//	������Ʈ��Ʈ�� ���� �ڽ��� ����� ���� ������Ʈ��Ʈ��
		//	������ ����ߴ�.
		//	������ ���� ������Ʈ��Ʈ�� ����� ����.
		//	1.	��ü �˻�
		String	sql = "insert into company values( ?, ?, ?, ?, ?, ?, ?, ?, ?,null,null,sysdate,?)";
//		String	sql = "insert into company1 values( ? )";
		pstmt = db.getPstmt(sql, con);
		}//setDB
	//=======================================================

	//*=======================================================
	//�����͸� �߰��� ���� �Լ�
	public void signProc() {
		//	����
		//		1.	�Է»��ڿ� �Էµ� ������ �˾Ƴ���.
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

		//��й�ȣ ��ȣȭ�ؼ� DB�� ����
		BASE64Encoder encoder = new BASE64Encoder();
		String encrypPass = encoder.encode(pass.getBytes());

		System.out.println("�����ȣ: "+empno);
		System.out.println("�̸�: "+name);
		System.out.println("�Ϲ� ��ȣ:"+pass+" ��ȣȭ: "+encrypPass);
		System.out.println("����: "+gen);
		System.out.println("�������:"+birth);
		System.out.println("��ȭ��ȣ:"+tel);
		System.out.println("�̸���: "+email);
		System.out.println("�μ�: "+cdept);
		System.out.println("���� :"+crank);
		System.out.println("����ip:"+ip);

		try {
			//	2.	�� ������ �����ͺ��̽����� ������.
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

			//3.	���� ��� �����Ѵ�.
			int query = pstmt.executeUpdate();
//			pstmt.execute();
			System.out.println("���� ���࿩��:"+query);
		}
		catch(Exception e) {
			System.out.println("��� ���� = " + e);
		}
	}
	//=======================================================


	//*=======================================================
	//�׽�Ʈ �Լ�
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
			System.out.println("�˻�Error: " + e);
		}
	}
	//=======================================================

	//*=======================================================
	public void closeProc(){
		System.out.println("���");
		this.setVisible(false);
		this.dispose();
	}

	//=======================================================

	//*=======================================================
	//��ư ���� �Լ�
	public void setButton() {
		signB = new JButton("ȸ������");
		cancelB = new JButton("����ϱ�");
		JPanel p14 = new JPanel(new FlowLayout());

		p14.add(signB);
		p14.add(cancelB);
		p14.setBackground(Color.white);
		this.add("South",p14);
		}
	//======================================================



	//*======================================================
	public void setInput(){
		JLabel empnoL = new JLabel("�����ȣ: ");
		empnoF = new JTextField();

		JLabel nameL = new JLabel("�̸�: ");
		nameF = new JTextField();

		JLabel passwordL = new JLabel("��й�ȣ: ");
		passwordF = new JPasswordField();
		passwordF.setEchoChar('��');

		JLabel genL = new JLabel("����: ");
		genC = new JComboBox();
		genC.addItem("��");
		genC.addItem("��");
		genC.setBackground(Color.WHITE);

		JLabel birthL = new JLabel("�������: ");
		birthF = new JTextField();

		JLabel telL = new JLabel("�ڵ���: ");
		telF = new JTextField();

		JLabel emailL = new JLabel("�̸���: ");
		emailF = new JTextField();

		JLabel cdeptL = new JLabel("�μ�: ");
		cdeptC = new JComboBox();
		cdeptC.addItem("�����ú�"); //Marketing
		cdeptC.addItem("��ȹ��"); //��ȹ��
		cdeptC.addItem("������"); //Sales
		cdeptC.addItem("���ߺ�"); //Develope

		JLabel crankL = new JLabel("����: ");
		crankC = new JComboBox();
		this.crankC.addItem("���");
		this.crankC.addItem("����");
		this.crankC.addItem("�븮");
		this.crankC.addItem("����");
		this.crankC.addItem("����");

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
		p11.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "ȸ������"));
		p11.add(new JLabel("���Ի�� �� �����ȣ �̹߱޽� �����ڿ��� �����ϼ���", JLabel.CENTER));
		JPanel p12 = new EPanel(20, 40);
		JPanel p13 = new EPanel(20, 40);

		this.add("Center",p15);
		//���� �ֱ�
		this.add("North",p11);
		this.add("West",p12);
		this.add("East",p13);
	}
	//======================================================


}

