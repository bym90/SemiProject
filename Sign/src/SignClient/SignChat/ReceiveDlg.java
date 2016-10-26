package SignClient.SignChat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ReceiveDlg extends JDialog {
	ChatCard main;
	TextArea area;
	JTextField whoF;
	JButton reB, closeB;
	// 상대 아이피 추가
	String otherIp;

	public ReceiveDlg(ChatCard m) {
		super(m);
		main = m;
		area = new TextArea();
		whoF = new JTextField();
		whoF.setEnabled(false);
		reB = new JButton("답장하기");
		closeB = new JButton("닫  기");
		ButtonEvent evt = new ButtonEvent();
		reB.addActionListener(evt);
		closeB.addActionListener(evt);

		JLabel whoL = new JLabel("보낸 사람 : ");
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add("West", whoL);
		p1.add("Center", whoF);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.add(reB);
		p2.add(closeB);

		this.add("North", p1);
		this.add("Center", area);
		this.add("South", p2);
	}

	public void reProc() {

		String name = whoF.getText();
		SendDlg2 dlg = new SendDlg2(main);
		dlg.otherIp = this.otherIp;
		dlg.whoF.setText(name);
		dlg.setSize(400, 300);
		dlg.setVisible(true);

		closeProc();
	}

	public void closeProc() {
		this.setVisible(false);
		this.dispose();
	}

	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == reB) {
				reProc();
			} else {
				closeProc();
			}
		}
	}
}


