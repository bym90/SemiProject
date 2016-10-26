package SignClient.SignNotice;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import SignClient.SignNotice.SignNotice.CloseEvent;
import javautil.*;

public class SignNoticeRead extends JDialog {
	public JTextField field;
	public JTextArea area;
	JButton exitB;
	public JLabel nameL;
	SignNotice snMain;

	public SignNoticeRead(SignNotice m) {
		super(m, "내용");
		snMain = m;
		this.addWindowListener(new CloseEvent());
		this.setLayout(new BorderLayout());
		field = new JTextField();
		field.setEditable(false);
		area = new JTextArea();
		area.setEditable(false);
		JScrollPane textPane = new JScrollPane(area);

		exitB = new JButton("닫기");
		exitB.addActionListener(new ButtonEvent());

		nameL = new JLabel("작성자");
		JLabel title = new JLabel("제목");
		JLabel content = new JLabel("내용");

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
		p4.add(exitB);

		this.add("Center", p3);
		this.add("South", p4);
	}

	// }
	// public static void main(String[] args){
	// new SignNoticeRead();
	// }

	public class CloseEvent extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			dispose();
		}
	}

	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

}
