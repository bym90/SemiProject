package SignClient.SignChat;

import java.awt.*;
import java.awt.event.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import javax.swing.*;


public class SendDlg extends JDialog {
	ChatCard main;
	TextArea area;
	JTextField whoF;
	JButton sendB, closeB;

	public SendDlg(ChatCard m) {
		super(m);
		main = m;
		area = new TextArea();
		whoF = new JTextField();
		whoF.setEnabled(false);
		sendB = new JButton("보내기");
		closeB = new JButton("닫  기");

		ButtonEvent evt = new ButtonEvent();
		sendB.addActionListener(evt);
		closeB.addActionListener(evt);

		JLabel whoL = new JLabel("받는 사람 : ");
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add("West", whoL);
		p1.add("Center", whoF);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.add(sendB);
		p2.add(closeB);

		this.add("North", p1);
		this.add("Center", area);
		this.add("South", p2);
	}

	public void sendProc() {
		// 보낼 내용을 Packet으로 포장
		String msg = area.getText() + "      " + main.main.name+main.main.empno; // 보낸 사람의 이름까지
																	// 버퍼에 넣어준다.
		// 받을 때 버퍼의 메시지 내용과 보낸사람의 이름을 분리하는 작업을 함
		byte[] buff = msg.getBytes();
		int len = buff.length;
		
		String name = whoF.getText();
		whoF.setText(name);

		// 상대방 주소를 넣는다
		String ip = main.ip;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ip);
		} catch (Exception e) {
		}
		// 상대방 포트번호
		int port = 4449;

		DatagramPacket packet = new DatagramPacket(buff, len, addr, port);

		try {
			// 패킷이 준비 되었으면 네트워크에 보내면 된다.
			main.sSocket.send(packet);
		} catch (Exception e) {
		}
		// 쪽지보내기 대화상자 닫음
		closeProc();
	}

	public void closeProc() {
		this.setVisible(false);
		this.dispose();
	}

	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == sendB) {
				sendProc();
			} else {
				closeProc();
			}
		}
	}
}

