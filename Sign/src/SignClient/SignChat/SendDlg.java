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
		sendB = new JButton("������");
		closeB = new JButton("��  ��");

		ButtonEvent evt = new ButtonEvent();
		sendB.addActionListener(evt);
		closeB.addActionListener(evt);

		JLabel whoL = new JLabel("�޴� ��� : ");
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
		// ���� ������ Packet���� ����
		String msg = area.getText() + "      " + main.main.name+main.main.empno; // ���� ����� �̸�����
																	// ���ۿ� �־��ش�.
		// ���� �� ������ �޽��� ����� ��������� �̸��� �и��ϴ� �۾��� ��
		byte[] buff = msg.getBytes();
		int len = buff.length;
		
		String name = whoF.getText();
		whoF.setText(name);

		// ���� �ּҸ� �ִ´�
		String ip = main.ip;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ip);
		} catch (Exception e) {
		}
		// ���� ��Ʈ��ȣ
		int port = 4449;

		DatagramPacket packet = new DatagramPacket(buff, len, addr, port);

		try {
			// ��Ŷ�� �غ� �Ǿ����� ��Ʈ��ũ�� ������ �ȴ�.
			main.sSocket.send(packet);
		} catch (Exception e) {
		}
		// ���������� ��ȭ���� ����
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

