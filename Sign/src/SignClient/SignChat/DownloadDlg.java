package SignClient.SignChat;
import		java.awt.*;
import		java.awt.event.*;
import		javax.swing.*;
import NetData.FileData;
import NetData.MainData;
import SignMainFrame.MainFrame;
public class DownloadDlg extends JDialog {
	MainFrame		main;
	public JList		list;
	JButton	downB, exitB;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public DownloadDlg(MainFrame m) {
		
		super(m);
		main = m;
		this.setTitle("파일 다운로드");
		this.setLayout(new BorderLayout());
		
		list = new JList();
		JScrollPane	sp = new JScrollPane(list);
		
		downB = new JButton("다운로드");
		exitB = new JButton("창 닫기");
		ButtonEvent	evt = new ButtonEvent();
		downB.addActionListener(evt);
		exitB.addActionListener(evt);
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p1.add(downB);
		p1.add(exitB);
		
		JPanel	p2 = new JPanel(new BorderLayout());
		p2.add("Center", sp);
		p2.add("South", p1);
			
		JPanel	northP = new JPanel(){
			public Dimension getPreferredSize() {
				return new Dimension(100, 20);
			}
		};
		JPanel	southP = new JPanel(){
			public Dimension getPreferredSize() {
				return new Dimension(100, 20);
			}
		};
		JPanel	eastP = new JPanel(){
			public Dimension getPreferredSize() {
				return new Dimension(15, 100);
			}
		};
		JPanel	westP = new JPanel(){
			public Dimension getPreferredSize() {
				return new Dimension(15, 100);
			}
		};
		
		this.add("North", northP);
		this.add("Center", p2);
		this.add("South", southP);
		this.add("West", westP);
		this.add("East", eastP);
	
		this.setLocation(((screenSize.width)/2)+250, ((screenSize.height)/2)-100);
	}
	

	//단추를 누르면 다운로드 요청
	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton	target = (JButton)e.getSource();
			if(target == downB) {
				//	서버에 선택한 파일의 이름을 다운로드 해달라고 부탁
				String		name = (String)list.getSelectedValue();
				MainData	data = new MainData();
				data.protocol = 1602;
				FileData	temp = new FileData();
				temp.fileName = name;
				data.fData = temp;
				
				try {
					main.oos.writeObject(data);
				}
				catch(Exception e1) {
					System.out.println("파일 다운 에러 = " + e1);
				}
			}
			else {
				main.dDlg.setVisible(false);
				main.dDlg.dispose();
			}
		}
	}
}




