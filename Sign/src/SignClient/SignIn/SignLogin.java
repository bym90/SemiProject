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
    
    /**������ ȹ��**/
    Dimension frameSize = this.getSize();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	/**������ ���**/
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("src/images/client/sign/Img_Login.png");
	
	public SignLogin(MainFrame m){ //MainFrame m ��ȣ�ȿ�
		super("Sign1.00 ver");
		main = m;
		img = new ImageIcon("src/images/client/common/login.gif");

		signB = new JButton("������");
		findB = new JButton("��й�ȣ ã��");
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		p2.setBackground(Color.white);
		p2.add(signB);
		p2.add(findB);
		
		JPanel p5 = new JPanel(new GridLayout(4,1));
		p5.setBackground(Color.white);
		idF = new JTextField("�����ȣ�� �Է����ּ���");
		pwF = new JPasswordField();
		pwF.setEchoChar('��');
		
		btn1 = new JButton("�α���");
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
		
		//��ư �̺�Ʈ ���
		ButtonEvent evt = new ButtonEvent();
		btn1.addActionListener(evt);
		signB.addActionListener(evt);
		findB.addActionListener(evt);
		
		//�ʵ� �̺�Ʈ ���
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
			if(target == btn1){//�α���
//				����
				//		���̵�� ����� �˾Ƴ���.
				String	empno = idF.getText();
				//	������ �������� ���Ἲ �˻縦 �Ѵ�.
				if(empno == null || empno.length() == 0) {
					return;
				}
				char[]	tempPW = pwF.getPassword();
				String	pw = new String(tempPW, 0, tempPW.length);
				if(pw == null || pw.length() == 0) {
					return;
				}
				System.out.println("�α��� ���̵�:"+empno+"\t��й�ȣ:"+pw);

				//		�˾Ƴ� ������ Ŭ������ �־ ������ ������. (�Կ��� �߰�����) 
				main.empno = empno; //�Կ�
				MainData data = new MainData();
				data.protocol = 1101;
				data.empno = empno.toUpperCase();
				data.pw = pw;
				try {
					main.oos.writeObject(data);
					System.out.println("�Լ����´�");
				}
				catch (Exception e1) {
					System.out.println("���� = " + e);
				}
			} else if (target == signB) {// ������
				new SignUpClient();
				
				
			} else if (target == findB) {// ��й�ȣã��
				new SignIdFind(main);
			} else {
				System.out.println("����� �ƹ���ư ����");
			}
		}
	}
	//*====================================================
	class MouseEvent01 extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(idF.getText().equals("�����ȣ�� �Է����ּ���")){
			idF.setText("");
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(idF.getText().equals("")){
				idF.setText("�����ȣ�� �Է����ּ���");
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
