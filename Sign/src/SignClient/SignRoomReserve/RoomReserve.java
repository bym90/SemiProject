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
	public RoomReserve(MainFrame m) { // 생성자
		this.setTitle("회의실 예약");
		main = m;
		setTable();
		setField();
		System.out.println("RoomReserve: entered constructor.");
	}
	
	// 처음 화면 뜨기 전에, 기존 룸 정보 table에 삽입. 
	public void addOneRow(Object[] obj){
		model.addRow(obj);
	}
	// '예약' 버튼 관련. DB에 업데이트 된 행 정보를 테이블에 추가.
	public void rowToTable(Object[][] obj){
		String[] columntitles = {"예약No","예약자","호실","정원","회의 목적","날짜","시작 시각","종료 시각","결재상태"};
		for(int i = 0; i < obj.length; i++) {
			System.out.println(obj[i][0]);
		}
		model.setDataVector(obj, columntitles);
	}
	//	테이블을 만들 method.
	public void setTable() {
		String[] title = {"예약No", "예약자","호실","정원", "회의 목적","날짜","시작 시각", "종료 시각","결재상태"};
		model = new DefaultTableModel(title, 0);
		table = new JTable(model);
		JScrollPane	sp = new JScrollPane(table);
		this.add("Center", sp);
	}
	//	폼을 만들 method.
	public void setField() {
		JLabel		roomL = new JLabel("방");
		JLabel		dateL = new JLabel("날짜");
		JLabel		purposeL = new JLabel("회의 목적");
		JLabel		startL = new JLabel("시작시각");
		JLabel		endL = new JLabel("종료시각");
		
		p1 = new JPanel(new GridLayout(5, 1));
		p1.add(roomL);
		p1.add(dateL);
		p1.add(purposeL);
		p1.add(startL);
		p1.add(endL);
		
		roomB = new JComboBox();
		roomB.addItem("※회의실 선택※");
		roomB.addItem("회의실A (20명)");
		roomB.addItem("회의실B (20명)");
		roomB.addItem("회의실C (20명)");
		roomB.addItem("회의실D (12명)");
		roomB.addItem("회의실E (12명)");
		roomB.addItem("회의실F (12명)");
		roomB.addItem("회의실G (08명)");
		roomB.addItem("회의실H (08명)");
		roomB.addItem("회의실I (08명)");
		
		roomB.addItemListener(new ComboEvent());
		
		dateF = new JTextField("형식:YYYYMMDD",11);
		purposeF = new JTextField(10);
		startF = new JTextField("형식:HHMM",10);
		endF = new JTextField("형식:HHMM",10);
		
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
		p4.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),"회의실 정보"));
		p4.setLayout(new BorderLayout());
		westP.add("South",p4);
		
		p7 = new JPanel(new GridLayout(1,2));
		confirmB = new JButton("예약하기");
		ButtonEvent1 btnEvent1 = new ButtonEvent1();
		confirmB.addActionListener(btnEvent1);
		
		p8 = new JPanel(new GridLayout(2,1));
		
		approveB = new JButton("승인");
		rejectB = new JButton("반려");
		deleteB = new JButton("삭제");
		
		approveB.addActionListener(btnEvent1);
		rejectB.addActionListener(btnEvent1);
		deleteB.addActionListener(btnEvent1);
		
		p8.add(approveB);
		p8.add(rejectB);
		p8.add(deleteB);
		p7.add(confirmB);
		p7.add(p8);
		westP.add("Center",p7);		
		
		JLabel		projectorL = new JLabel("빔프로젝터");
		JLabel		micL = new JLabel("마이크");
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
	//예약 정보 입력시, 텍스트 필드에 입력 형식을 알려주도록 하는 method. 
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
						null, new JLabel("<html><body><font color='red'>날짜형태</font>가 형식에 맞지 않습니다.<br/>"
							+ "형태 : YYYYMMDD형태로 입력해주세요.</body></html>"), "경고!!", JOptionPane.ERROR_MESSAGE);
				}
			}else if(eventedField == startF || eventedField == endF){
				boolean dateCheck = JUtil.isTime(temp);
				if(dateCheck != true){
					JOptionPane.showMessageDialog(
						null, new JLabel("<html><body><font color='red'>시간형태</font>가 형식에 맞지 않습니다.<br/>"
							+ "형태 : HHMM형태로 입력해주세요.</body></html>"), "경고!!", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	//회의실 콤보상자에서 선택시에, 방 보유 장비 현황을 DB에서 출력. 
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
	// 예약, 승인, 반려, 삭제 버튼에 관한 동작 정의.
	class ButtonEvent1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// 1. "예약"버튼 누를 때.
			if (e.getSource() == confirmB) {
				//칸에 정보 넣지 않았을 때에는, 버튼을 눌러도 아무것도 실행 안 됨.
				if (((String) roomB.getSelectedItem()).equals("※회의실 선택※")) {return;}
				if (dateF.getText().trim().equals("형식:YYYYMMDD")) {return;}
				if (startF.getText().equals("형식:HHMM")) {return;}
				if (endF.getText().equals("형식:HHMM")) {return;}
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
				System.out.println(reservedata.empno);//데이터 들어갔음.
				System.out.println(reservedata.people); //인원수 확인.
				data.rData = reservedata; //DataBean인, MainData에 삽입. 
				data.protocol = 3101;
				try {
					main.oos.writeObject(data);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				// 승인, 반려, 삭제 버튼 누를 때. 
			} else if(e.getSource() == approveB || e.getSource() == rejectB || e.getSource() == deleteB){
				String rank = main.crank.trim();
				System.out.println(rank);
				if(rank.equals("과장") || rank.equals("부장")){ //과장이나 부장만, 승인, 반려, 삭제를 할 수 있다. 
					int row = table.getSelectedRow();
					int cnumValue = (int) table.getValueAt(row, 0); //cnumValue 값 들어감 확인됨. 
					MainData data = new MainData();
					ReserveData reservedata = new ReserveData();
					reservedata.cnumber = cnumValue;
					if(e.getSource() == approveB){ // 승인, 반려, 삭제 선택 정보를 각각 1,2,3으로 자료콩에 기록. 
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
					return;  //과장, 부장이 아닌 경우, 이벤트는 실행되지 않는다. 
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