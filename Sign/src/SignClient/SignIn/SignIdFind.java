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
	
	//������ ����� �˾Ƴ��� ���� ����
	Dimension frameSize = this.getSize();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	//���̵�� ��й�ȣ�� ã�� ���� ����
	PreparedStatement	pstmt , findS;
	ResultSet			rs;
	SignIdFind(MainFrame m) {
		super(m);
		main = m;
		this.setTitle("ȸ������");
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.setBackground(Color.white);
		img = new ImageIcon("src/images/client/sign/passkakao.gif");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image icon = toolkit.getImage("src/images/client/sign/Img_PassFind.png");
		
		JPanel p5 = new JPanel(new GridLayout(3, 1));
		p5.setBackground(Color.white);

		idF = new JTextField("�����ȣ�� �Է����ּ���");
		emailF = new JTextField("�̸����� �Է����ּ���");

		btn1 = new JButton("��й�ȣ ã��");
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

		// ��ư �̺�Ʈ ���
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
	class MouseEvent02 extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		System.out.println(emailF.getText());
		System.out.println(emailF.getText().trim());

		if(emailF.getText().equals("�̸����� �Է����ּ���")){
			emailF.setText("");
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(emailF.getText().equals("")){
			emailF.setText("�̸����� �Է����ּ���");
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
			}else{ // ��ư�� ��
				JButton temp = (JButton) e.getSource();
				if(temp == btn1){
					//����
					//		���̵�� ����� �˾Ƴ���.
					String empno = idF.getText();
					String email = emailF.getText();
					//	������ �������� ���Ἲ �˻縦 �Ѵ�.
					if(idF.getText() == null || idF.getText().length() == 0|| idF.getText().equals("�����ȣ�� �Է����ּ���")) {
						int kind = JOptionPane.showConfirmDialog(null, "�����ȣ�� �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
						if (kind == JOptionPane.YES_OPTION) {
						return;
						}
					}
					if(emailF.getText() == null || emailF.getText().length() == 0 || emailF.getText().equals("�̸����� �Է����ּ���")) {
						int kind = JOptionPane.showConfirmDialog(null, "�̸����� �߸��Ǿ����ϴ�.", "���", JOptionPane.DEFAULT_OPTION,0,new ImageIcon("src/images/client/sign/warning2.png"));
						if (kind == JOptionPane.YES_OPTION) {
						return;
						}
					}
					//�˾Ƴ� ������ Ŭ������ �־ ������ ������.
					MainData	data = new MainData();
					data.protocol = 1102;	//��� ã��
					data.empno = empno.trim().toUpperCase();
					data.email = email.trim().toLowerCase();
					System.out.println("�����ȣ:"+data.empno+"\t�̸���:"+data.email);
					try {
						main.oos.writeObject(data);
					}
					catch(Exception e1) {
						System.out.println("���ã�� ����: " + e1);
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

	//*=====================�Լ�=================
	public void closeProc(){
		System.out.println("���");
		this.setVisible(false);
		this.dispose();
	}
	//=====================�Լ�=================
//	public static void main(String[] args) {
//		new SignIdFind();
//	}
}
