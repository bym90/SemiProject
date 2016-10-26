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
		this.setTitle("���� �ٿ�ε�");
		this.setLayout(new BorderLayout());
		
		list = new JList();
		JScrollPane	sp = new JScrollPane(list);
		
		downB = new JButton("�ٿ�ε�");
		exitB = new JButton("â �ݱ�");
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
	

	//���߸� ������ �ٿ�ε� ��û
	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton	target = (JButton)e.getSource();
			if(target == downB) {
				//	������ ������ ������ �̸��� �ٿ�ε� �ش޶�� ��Ź
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
					System.out.println("���� �ٿ� ���� = " + e1);
				}
			}
			else {
				main.dDlg.setVisible(false);
				main.dDlg.dispose();
			}
		}
	}
}




