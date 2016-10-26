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
	// 쪽지 보낼 때 상대방 아이피 저장
	public String ip;

	public ChatCard(MainFrame m) {
		super(m);
		main = m;
		
		this.setTitle("부서 채팅 & 쪽지");
		this.setLayout(new BorderLayout());
		area = new JTextArea();
//		area.enable(false);
		area.disable();
		area.setDisabledTextColor(Color.BLACK);
		sp = new JScrollPane(area);
		
		
		field = new JTextField();
		sendB = new JButton("대화하기");
		exitB = new JButton("방 나가기");
		upB = new JButton("파일업로드");
		downB = new JButton("파일다운로드");
		
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
			// 메시지 주고 받기를 위한 UDP통신
			sSocket = new DatagramSocket();
			rSocket = new DatagramSocket(4449);

			MessageReceiveThread t = new MessageReceiveThread(this);
			t.start();

		} catch (Exception e) {
			System.out.println("메시지 소켓 에러 = " + e);
		}

		root = new DefaultMutableTreeNode("회사 직원들");
		dept1 = new DefaultMutableTreeNode("영업부");
		dept2 = new DefaultMutableTreeNode("마케팅부");
		dept3 = new DefaultMutableTreeNode("기획부");
		dept4 = new DefaultMutableTreeNode("개발부");

		myTree = new JTree(root);
		myTree.setBorder(new TitledBorder("쪽지 보내기"));
		myTree.addTreeSelectionListener(new JTreeEvent());
	
		sp2 = new JScrollPane(myTree);
		sp2.setPreferredSize(new Dimension(150, 500));
		this.add("West", sp2);
	
		//노드 이미지 설정 
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(new ImageIcon("src/SignClient/signChat/dept.png"));
		renderer.setClosedIcon(new ImageIcon("src/SignClient/signChat/dept.png"));
		renderer.setLeafIcon(new ImageIcon("src/SignClient/signChat/emp.jpg"));
		myTree.setCellRenderer(renderer);
	}

	//접속한 회원을 쪽지 목록에 추가
	public void empAddProc(){
		
		String temp = "";
		
		//접속한 영업부 사원을 노드에 추가
		for (int i = 0; i < chatSname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatSname[i].substring(chatSname[i].length() - 5, chatSname[i].length());

				if (temp.equals(Emps[j])) {
					dept1.add(new DefaultMutableTreeNode(chatSname[i]));
				}
			}
		}
		root.add(dept1);
	
		//접속한 마케팉부 사원을 노드에 추가
		for (int i = 0; i < chatMname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatMname[i].substring(chatMname[i].length() - 5, chatMname[i].length());

				if (temp.equals(Emps[j])) {
					dept2.add(new DefaultMutableTreeNode(chatMname[i]));
				}
			}
		}
		root.add(dept2);
	
		//접속한 생산부 사원을 노드에 추가
		for (int i = 0; i < chatPname.length; i++) {
			for (int j = 0; j < Emps.length; j++) {
				temp = chatPname[i].substring(chatPname[i].length() - 5, chatPname[i].length());

				if (temp.equals(Emps[j])) {
					dept3.add(new DefaultMutableTreeNode(chatPname[i]));
				}
			}
		}
		root.add(dept3);
		
		//접속한 인사부 사원을 노드에 추가
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
	
	//쪽지 목록 갱신
	public void renewProc() {
		dept1.removeAllChildren();
		dept2.removeAllChildren();
		dept3.removeAllChildren();
		dept4.removeAllChildren();
	
		empAddProc();
	}

	//채팅 전송
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

	//채팅방 나가기
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

	//업로드
	public void upProc() {
		
		// 보낼 파일을 선택
		JFileChooser fc = new JFileChooser("D:\\");
		fc.setLocation(((screenSize.width)/2-150), ((screenSize.height)/2)-150);
		fc.setVisible(true);
		int kind = fc.showOpenDialog(main);
		
		
		// 파일을 보이는 창을 띄우고 확인단추를 누르지 않으면 리턴
		if (kind != JFileChooser.APPROVE_OPTION) {
			return;
		}
		// 파일의 이름과 내용을 알아냄
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

	//파일 다운로드 요청
	public void downProc() {
		// 목록을 보내달라고 요청
		MainData data = new MainData();
		data.protocol = 1601;
		try {
			main.oos.writeObject(data);
		} catch (Exception e) {}
	}

	//노드 클릭시 클릭된 해당 사원번호를 알아내서 ip주소를 알아온다. 
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
				// 채팅보내기
				sendProc();
			} else {
				JButton temp = (JButton) target;
				if (temp == sendB) {
					// 채팅 보내기
					sendProc();
				} else if (temp == exitB) {
					// 방나가기
					exitProc();
				} else if (temp == upB) {
					// 업로드
					upProc();
				} else {
					// 다운로드
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
						// 쪽지 클릭 시 상대방 아이피를 따오기 위해서 사원번호를 따서 보내줌
						otherEmpno = Othername.substring(Othername.length() - 5, Othername.length());
						getIp();
						//메시지 보내기 다이어로그 
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
