package SignClient.SignChat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.tree.*;
import NetData.*;
import SignMainFrame.MainFrame;
import java.io.*;
import java.net.DatagramSocket;

public class ChatCard extends JDialog {

	public JTextArea area;
	JScrollPane sp, sp2;
	JTextField field;
	JButton sendB, exitB, upB, downB,set;
	MainFrame main;
	String Othername;
	public String NameEmpno;

	public int deptsize;
	public int Snamesize;
	public int Mnamesize;
	public int Pnamesize;
	public int Dnamesize;
	public int EmpsSize;

	public String[] Emps = new String[EmpsSize];
	public String[] chatdept = new String[deptsize];
	public String[] chatSname = new String[Snamesize];
	public String[] chatMname = new String[Mnamesize];
	public String[] chatPname = new String[Pnamesize];
	public String[] chatDname = new String[Dnamesize];

	DefaultTreeModel model;
	DefaultMutableTreeNode root,dept1,dept2,dept3,dept4;

	JTree myTree;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	DatagramSocket rSocket, sSocket;
	String otherEmpno;
	// ���� ���� �� ���� ������ ����
	public String ip;

	public ChatCard(MainFrame m) {
		super(m);
		main = m;
		
		this.setTitle("�μ� ä�� & ����");
		this.setLayout(new BorderLayout());
		area = new JTextArea();
//		area.enable(false);
		area.disable();
		area.setDisabledTextColor(Color.BLACK);
		sp = new JScrollPane(area);
		
		
		field = new JTextField();
		sendB = new JButton("��ȭ�ϱ�");
		exitB = new JButton("�� ������");
		upB = new JButton("���Ͼ��ε�");
		downB = new JButton("���ϴٿ�ε�");
		
		ButtonEvent evt = new ButtonEvent();
		
		field.addActionListener(evt);
		sendB.addActionListener(evt);
		exitB.addActionListener(evt);
		downB.addActionListener(evt);
		upB.addActionListener(evt);

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add("Center", field);
		p1.add("East", sendB);

		JPanel p2 = new JPanel();
		p2.add(upB);
		p2.add(downB);

		JPanel p4 = new JPanel();
		p4.add(exitB);

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add("West", p2);
		p3.add("East", p4);

		this.add("North", p3);
		this.add("Center", sp);
		this.add("South", p1);
	
		try {
			// �޽��� �ְ� �ޱ⸦ ���� UDP���
			sSocket = new DatagramSocket();
			rSocket = new DatagramSocket(4449);

			MessageReceiveThread t = new MessageReceiveThread(this);
			t.start();

		} catch (Exception e) {
			System.out.println("�޽��� ���� ���� = " + e);
		}

		root = new DefaultMutableTreeNode("ȸ�� ������");
		dept1 = new DefaultMutableTreeNode("������");
		dept2 = new DefaultMutableTreeNode("�����ú�");
		dept3 = new DefaultMutableTreeNode("��ȹ��");
		dept4 = new DefaultMutableTreeNode("���ߺ�");

		myTree = new JTree(root);
		myTree.setBorder(new TitledBorder("���� ������"));
		myTree.addTreeSelectionListener(new JTreeEvent());
	
		sp2 = new JScrollPane(myTree);
		sp2.setPreferredSize(new Dimension(150, 500));
		this.add("West", sp2);
	
		//��� �̹��� ���� 
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(new ImageIcon("src/SignClient/signChat/dept.png"));
		renderer.setClosedIcon(new ImageIcon("src/SignClient/signChat/dept.png"));
		renderer.setLeafIcon(new ImageIcon("src/SignClient/signChat/emp.jpg"));
		myTree.setCellRenderer(renderer);
	}

	//������ ȸ���� ���� ��Ͽ� �߰�
	public void empAddProc(){
		
		String temp = "";
		
		//������ ������ ����� ��忡 �߰�
		for (int i = 0; i < chatSname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatSname[i].substring(chatSname[i].length() - 5, chatSname[i].length());

				if (temp.equals(Emps[j])) {
					dept1.add(new DefaultMutableTreeNode(chatSname[i]));
				}
			}
		}
		root.add(dept1);
	
		//������ ���ɻK�� ����� ��忡 �߰�
		for (int i = 0; i < chatMname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatMname[i].substring(chatMname[i].length() - 5, chatMname[i].length());

				if (temp.equals(Emps[j])) {
					dept2.add(new DefaultMutableTreeNode(chatMname[i]));
				}
			}
		}
		root.add(dept2);
	
		//������ ����� ����� ��忡 �߰�
		for (int i = 0; i < chatPname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatPname[i].substring(chatPname[i].length() - 5, chatPname[i].length());

				if (temp.equals(Emps[j])) {
					dept3.add(new DefaultMutableTreeNode(chatPname[i]));
				}
			}
		}
		root.add(dept3);
		
		//������ �λ�� ����� ��忡 �߰�
		for (int i = 0; i < chatDname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatDname[i].substring(chatDname[i].length() - 5, chatDname[i].length());

				if (temp.equals(Emps[j])) {
					dept4.add(new DefaultMutableTreeNode(chatDname[i]));
				}
			}
		}
		root.add(dept4);
		
		model = (DefaultTreeModel)myTree.getModel();
		model.reload();
	}
	
	//���� ��� ����
	public void renewProc() {
		dept1.removeAllChildren();
		dept2.removeAllChildren();
		dept3.removeAllChildren();
		dept4.removeAllChildren();
	
		empAddProc();
	}

	//ä�� ����
	public void sendProc() {
		String msg = field.getText().trim();
		if (msg == null || msg.length() == 0) {
			return;
		}
		MainData data = new MainData();
		data.protocol = 1401;
		ChatData temp = new ChatData();
		temp.msg = msg;
		data.cData = temp;
		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
		
		field.setText("");
	}

	//ä�ù� ������
	public void exitProc() {

		MainData data = new MainData();
		data.protocol = 1302;
		ChatData temp = new ChatData();
		temp.room = main.room;
		data.cData = temp;
		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
	}

	//���ε�
	public void upProc() {
		
		// ���� ������ ����
		JFileChooser fc = new JFileChooser("D:\\");
		fc.setLocation(((screenSize.width)/2-150), ((screenSize.height)/2)-150);
		fc.setVisible(true);
		int kind = fc.showOpenDialog(main);
		
		
		// ������ ���̴� â�� ���� Ȯ�δ��߸� ������ ������ ����
		if (kind != JFileChooser.APPROVE_OPTION) {
			return;
		}
		// ������ �̸��� ������ �˾Ƴ�
		File file = fc.getSelectedFile();
		String fileName = file.getName();
		long size = file.length();
		byte[] buff = new byte[(int) size];
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			fin.read(buff);
		} catch (Exception e) {}

		finally {
			try {
				fin.close();

			} catch (Exception e) {}
		} 
		
	
		MainData data = new MainData();
		data.protocol = 1501;
		FileData temp = new FileData();
		temp.fileName = fileName;
		temp.buff = buff;
		data.fData = temp;
	
		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
	}

	//���� �ٿ�ε� ��û
	public void downProc() {
		// ����� �����޶�� ��û
		MainData data = new MainData();
		data.protocol = 1601;
		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
	}

	//��� Ŭ���� Ŭ���� �ش� �����ȣ�� �˾Ƴ��� ip�ּҸ� �˾ƿ´�. 
	public void getIp() {
		MainData data = new MainData();
		data.protocol = 1701;
		MessageData temp = new MessageData();
		temp.Empno = otherEmpno;
		data.mData = temp;

		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
	}

	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object target = e.getSource();
			if (target instanceof JTextField) {
				// ä�ú�����
				sendProc();
			} else {
				JButton temp = (JButton) target;
				if (temp == sendB) {
					// ä�� ������
					sendProc();
				} else if (temp == exitB) {
					// �泪����
					exitProc();
				} else if (temp == upB) {
					// ���ε�
					upProc();
				} else {
					// �ٿ�ε�
					downProc();
				}
			}
		}
	}
	class JTreeEvent implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {

			try {
				if (e.getSource() == myTree) {
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
					if (selNode == root || selNode == dept1 || selNode == dept2 || selNode == dept3 || selNode == dept4){
						return;
					}
					else {
						Othername = selNode.toString();
						// ���� Ŭ�� �� ���� �����Ǹ� ������ ���ؼ� �����ȣ�� ���� ������
						otherEmpno = Othername.substring(Othername.length() - 5, Othername.length());
						getIp();
						//�޽��� ������ ���̾�α� 
						SendDlg dlg = new SendDlg(ChatCard.this);
						dlg.whoF.setText(Othername);
						dlg.setSize(400, 300);
						dlg.setVisible(true);
					}
				}
			}
				catch (Exception e2) {}
		}
	}
}
