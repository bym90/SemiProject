package SignClient.SignRoomReserve;

import		java.awt.*;
import 		java.awt.event.*;
import		javax.swing.*;
import 		javax.swing.border.*;
import		javax.swing.table.*;

import		java.net.*;
import		java.io.*;
import		javautil.*;
import		NetData.*;
import		SignMainFrame.MainFrame;
public class RoomReserve extends JDialog {
	MainFrame main;
	JTable				table;
	DefaultTableModel	model;
	JTextField			dateF, purposeF, startF, endF;
	JButton				confirmB;
	public JButton approveB, rejectB, deleteB;
	JComboBox			roomB;
	JPanel p1,p2,p3,p4,p5,p6,p7,p8,westP;
	public JLabel projector,mic,PC;
	Socket socket;
	PrintWriter pw;
	BufferedReader br;
	public RoomReserve(MainFrame m) { // ������
		this.setTitle("ȸ�ǽ� ����");
		main = m;
		setTable();
		setField();
		System.out.println("RoomReserve: entered constructor.");
	}
	
	// ó�� ȭ�� �߱� ����, ���� �� ���� table�� ����. 
	public void addOneRow(Object[] obj){
		model.addRow(obj);
	}
	// '����' ��ư ����. DB�� ������Ʈ �� �� ������ ���̺� �߰�.
	public void rowToTable(Object[][] obj){
		String[] columntitles = {"����No","������","ȣ��","����","ȸ�� ����","��¥","���� �ð�","���� �ð�","�������"};
		for(int i = 0; i < obj.length; i++) {
			System.out.println(obj[i][0]);
		}
		model.setDataVector(obj, columntitles);
	}
	//	���̺��� ���� method.
	public void setTable() {
		String[] title = {"����No", "������","ȣ��","����", "ȸ�� ����","��¥","���� �ð�", "���� �ð�","�������"};
		model = new DefaultTableModel(title, 0);
		table = new JTable(model);
		JScrollPane	sp = new JScrollPane(table);
		this.add("Center", sp);
	}
	//	���� ���� method.
	public void setField() {
		JLabel		roomL = new JLabel("��");
		JLabel		dateL = new JLabel("��¥");
		JLabel		purposeL = new JLabel("ȸ�� ����");
		JLabel		startL = new JLabel("���۽ð�");
		JLabel		endL = new JLabel("����ð�");
		
		p1 = new JPanel(new GridLayout(5, 1));
		p1.add(roomL);
		p1.add(dateL);
		p1.add(purposeL);
		p1.add(startL);
		p1.add(endL);
		
		roomB = new JComboBox();
		roomB.addItem("��ȸ�ǽ� ���á�");
		roomB.addItem("ȸ�ǽ�A (20��)");
		roomB.addItem("ȸ�ǽ�B (20��)");
		roomB.addItem("ȸ�ǽ�C (20��)");
		roomB.addItem("ȸ�ǽ�D (12��)");
		roomB.addItem("ȸ�ǽ�E (12��)");
		roomB.addItem("ȸ�ǽ�F (12��)");
		roomB.addItem("ȸ�ǽ�G (08��)");
		roomB.addItem("ȸ�ǽ�H (08��)");
		roomB.addItem("ȸ�ǽ�I (08��)");
		
		roomB.addItemListener(new ComboEvent());
		
		dateF = new JTextField("����:YYYYMMDD",11);
		purposeF = new JTextField(10);
		startF = new JTextField("����:HHMM",10);
		endF = new JTextField("����:HHMM",10);
		
		FocusEvent1 focusevent1 = new FocusEvent1();
		dateF.addFocusListener(focusevent1);
		startF.addFocusListener(focusevent1);
		endF.addFocusListener(focusevent1);
		
		p2 = new JPanel(new GridLayout(5, 1));
		p2.add(roomB);
		p2.add(dateF);
		p2.add(purposeF);
		p2.add(startF);
		p2.add(endF);
		
		p3 = new JPanel(new BorderLayout());
		p3.add("West", p1);
		p3.add("Center", p2);
		
		westP = new JPanel(new BorderLayout());
		westP.add("North",p3);
		
		p4 = new JPanel();
		p4.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),"ȸ�ǽ� ����"));
		p4.setLayout(new BorderLayout());
		westP.add("South",p4);
		
		p7 = new JPanel(new GridLayout(1,2));
		confirmB = new JButton("�����ϱ�");
		ButtonEvent1 btnEvent1 = new ButtonEvent1();
		confirmB.addActionListener(btnEvent1);
		
		p8 = new JPanel(new GridLayout(2,1));
		
		approveB = new JButton("����");
		rejectB = new JButton("�ݷ�");
		deleteB = new JButton("����");
		
		approveB.addActionListener(btnEvent1);
		rejectB.addActionListener(btnEvent1);
		deleteB.addActionListener(btnEvent1);
		
		p8.add(approveB);
		p8.add(rejectB);
		p8.add(deleteB);
		p7.add(confirmB);
		p7.add(p8);
		westP.add("Center",p7);		
		
		JLabel		projectorL = new JLabel("����������");
		JLabel		micL = new JLabel("����ũ");
		JLabel		PCL = new JLabel("PC");
		
		projector = new JLabel("");
		mic = new JLabel("");
		PC = new JLabel("");
		
		p5 = new JPanel();
		p5.setLayout(new GridLayout(3,1));
		p5.add(projectorL);
		p5.add(micL);
		p5.add(PCL);
		
		p6 = new JPanel();
		p6.setLayout(new GridLayout(3,1));
		p6.add(projector);
		p6.add(mic);
		p6.add(PC);
		
		p4.add("West", p5);
		p4.add("Center", p6);
		p6.setVisible(false);
		
		this.add("West", westP);
	}
	//���� ���� �Է½�, �ؽ�Ʈ �ʵ忡 �Է� ������ �˷��ֵ��� �ϴ� method. 
	class FocusEvent1 implements FocusListener{
		public void focusGained(FocusEvent e) {
			JTextField selectedTextField = (JTextField)e.getSource();
			selectedTextField.setText("");
		}
		@Override
		public void focusLost(FocusEvent e) {
			JTextField eventedField = (JTextField) e.getSource();
			String temp = eventedField.getText().trim();
			if(temp == null || temp.length() == 0){return;}
			if(eventedField == dateF){
				boolean dateCheck = JUtil.isDate(temp);
				if(dateCheck != true){
					JOptionPane.showMessageDialog(
						null, new JLabel("<html><body><font color='red'>��¥����</font>�� ���Ŀ� ���� �ʽ��ϴ�.<br/>"
							+ "���� : YYYYMMDD���·� �Է����ּ���.</body></html>"), "���!!", JOptionPane.ERROR_MESSAGE);
				}
			}else if(eventedField == startF || eventedField == endF){
				boolean dateCheck = JUtil.isTime(temp);
				if(dateCheck != true){
					JOptionPane.showMessageDialog(
						null, new JLabel("<html><body><font color='red'>�ð�����</font>�� ���Ŀ� ���� �ʽ��ϴ�.<br/>"
							+ "���� : HHMM���·� �Է����ּ���.</body></html>"), "���!!", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	//ȸ�ǽ� �޺����ڿ��� ���ýÿ�, �� ���� ��� ��Ȳ�� DB���� ���. 
	class ComboEvent implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			String str1 = (String)roomB.getSelectedItem();
			String roomname = str1.substring(3, 4).trim();
			System.out.println(roomname);	
			MainData data=new MainData();
			RoomData temp=new RoomData();
			temp.roomname = roomname;
			data.roData = temp;
			data.protocol = 3002;
			System.out.println(data.roData.roomname);
			try {
				main.oos.writeObject(data);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			p6.setVisible(true);
		}
	}
	// ����, ����, �ݷ�, ���� ��ư�� ���� ���� ����.
	class ButtonEvent1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// 1. "����"��ư ���� ��.
			if (e.getSource() == confirmB) {
				//ĭ�� ���� ���� �ʾ��� ������, ��ư�� ������ �ƹ��͵� ���� �� ��.
				if (((String) roomB.getSelectedItem()).equals("��ȸ�ǽ� ���á�")) {return;}
				if (dateF.getText().trim().equals("����:YYYYMMDD")) {return;}
				if (startF.getText().equals("����:HHMM")) {return;}
				if (endF.getText().equals("����:HHMM")) {return;}
				if (dateF.getText() == null || dateF.getText().length() == 0) {return;}
				MainData data = new MainData();
				ReserveData reservedata = new ReserveData();
				String str1 = (String) roomB.getSelectedItem();
				reservedata.roomNum = str1.substring(3, 4).trim();
				reservedata.reserDay = dateF.getText().trim();
				reservedata.purpose = purposeF.getText().trim();
				reservedata.startingTime = startF.getText().trim();
				reservedata.endingTime = endF.getText().trim();
				reservedata.empno = main.empno;
				reservedata.name = main.name;
				String str2 = (String) roomB.getSelectedItem();
				reservedata.people = Integer.parseInt(str2.substring(6, 8));
				System.out.println(reservedata.empno);//������ ����.
				System.out.println(reservedata.people); //�ο��� Ȯ��.
				data.rData = reservedata; //DataBean��, MainData�� ����. 
				data.protocol = 3101;
				try {
					main.oos.writeObject(data);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				// ����, �ݷ�, ���� ��ư ���� ��. 
			} else if(e.getSource() == approveB || e.getSource() == rejectB || e.getSource() == deleteB){
				String rank = main.crank.trim();
				System.out.println(rank);
				if(rank.equals("����") || rank.equals("����")){ //�����̳� ���常, ����, �ݷ�, ������ �� �� �ִ�. 
					int row = table.getSelectedRow();
					int cnumValue = (int) table.getValueAt(row, 0); //cnumValue �� �� Ȯ�ε�. 
					MainData data = new MainData();
					ReserveData reservedata = new ReserveData();
					reservedata.cnumber = cnumValue;
					if(e.getSource() == approveB){ // ����, �ݷ�, ���� ���� ������ ���� 1,2,3���� �ڷ��ῡ ���. 
						reservedata.whichButton = 1;
					} else if(e.getSource() == rejectB){
						reservedata.whichButton = 2;
					} else if(e.getSource() == deleteB){
						reservedata.whichButton = 3;
					}
					data.rData = reservedata;
					data.protocol = 3103;
					try {
						main.oos.writeObject(data);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println("Entered else block.");
					return;  //����, ������ �ƴ� ���, �̺�Ʈ�� ������� �ʴ´�. 
				}
			} else {return;}
		}
	}
	class CloseEvent extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			closeProc();
		}
	}
	public void closeProc(){
		this.dispose();
		this.setVisible(false);
	}
	
}