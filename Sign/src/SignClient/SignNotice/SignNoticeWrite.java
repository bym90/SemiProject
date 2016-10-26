package SignClient.SignNotice;

import javautil.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import NetData.*;
import SignMainFrame.MainFrame;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class SignNoticeWrite extends JDialog {
	public JTextField field;
	public JTextArea area;
	JButton saveB, cancelB;
	SignNotice snMain;
	public Integer nnum;
	public JLabel nameL;

	public SignNoticeWrite(SignNotice m) {
		super(m, "����");
		snMain = m;

		this.setLayout(new BorderLayout());
		field = new JTextField();
		area = new JTextArea();
		JScrollPane textPane = new JScrollPane(area);

		saveB = new JButton("����");
		cancelB = new JButton("���");
		ButtonEventSave evt = new ButtonEventSave();
		saveB.addActionListener(evt);
		cancelB.addActionListener(evt);

		nameL = new JLabel("�ۼ���");
		JLabel title = new JLabel("����");
		JLabel content = new JLabel("����");

		JPanel p0 = new JPanel(new BorderLayout());
		p0.add("West", title);
		p0.add("East", nameL);

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add("North", p0);
		p1.add("Center", field);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add("North", content);
		p2.add("Center", textPane);

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add("North", p1);
		p3.add("Center", p2);

		JPanel p4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p4.add(saveB);
		p4.add(cancelB);

		this.add("Center", p3);
		this.add("South", p4);

	}

	// public static void main(String[] args) {
	// new SignNoticeWrite();
	//
	// }

	class ButtonEventSave implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			BufferedReader br = null;
			if (target == saveB) {
				if (snMain.main.buttonInfo.equals("�۾���")) {
					MainData data = new MainData();
					data.protocol = 4201;
					data.name = snMain.main.name;
					data.empno = snMain.main.empno;
					NoticeData temp = new NoticeData();
					temp.title = field.getText();
					System.out.println("------�������ͺ� �ֱ�" + field.getText());
					temp.content = area.getText();
					System.out.println("------���뵥���ͺ� �ֱ�" + area.getText());
					data.nData = temp;
					try {
						snMain.main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else if (snMain.main.buttonInfo.equals("����")) {
					nnum = snMain.main.nnum;
					MainData data = new MainData();
					data.protocol = 4401;
					data.name = snMain.main.name;
					data.empno = snMain.main.name;
					NoticeData temp = new NoticeData();
					temp.title = field.getText();
					temp.content = area.getText();
					temp.nNum = nnum;
					data.nData = temp;
					try {
						snMain.main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

			} else {
				dispose();
			}
		}
	}
}
